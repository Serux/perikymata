package es.ubu.lsi.perikymata.vista;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.text.DefaultEditorKit.CopyAction;

import com.sun.javafx.iio.ios.IosDescriptor;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;

import es.ubu.lsi.perikymata.MainApp;
import ij.io.Opener;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class ImageSelectionController {

	/**
	 * preview of the full tooth Image.
	 */
	@FXML
	private ImageView previewImage;

	/**
	 * List of selected images to stitch.
	 */
	@FXML
	private ListView<String> filesListView;

	/**
	 * Reference to the main application.
	 */
	private MainApp mainApp;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		previewImage.fitHeightProperty().bind(((Pane) previewImage.getParent()).heightProperty());
		previewImage.fitWidthProperty().bind(((Pane) previewImage.getParent()).widthProperty());


	}

	/**
	 * Opens a FileChooser to let the user select Image to load.
	 */
	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {

			java.awt.Image full = new Opener().openImage(file.getParent(), file.getName()).getImage();

			mainApp.setFullImage(SwingFXUtils.toFXImage((BufferedImage) full, null));
			previewImage.setImage(mainApp.getFullImage());
			mainApp.setFilteredImage(mainApp.getFullImage());

		}
	}

	/**
	 * Opens a FileChooser to let the user select multiple Images to load.
	 */
	@FXML
	private void handleOpenMultiple() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		List<File> list = fileChooser.showOpenMultipleDialog(mainApp.getPrimaryStage());
		for (File file : list) {
			if (file != null) {
				mainApp.getFilesList().add(file.getName());

				try (FileOutputStream fileStream = new FileOutputStream(
						new File(Paths.get(mainApp.getProjectPath(), "Fragments").toString(), file.getName()))) {

					Files.copy(file.toPath(), fileStream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * Handler that rotates image.
	 */
	@FXML
	private void handleRotate() {
		BufferedImage im = SwingFXUtils.fromFXImage(previewImage.getImage(), null);
		AffineTransform a = new AffineTransform();
		a.translate(0.5 * im.getHeight(), 0.5 * im.getWidth());
		a.rotate(Math.PI / 2);
		a.translate(-0.5 * im.getWidth(), -0.5 * im.getHeight());
		AffineTransformOp op = new AffineTransformOp(a, AffineTransformOp.TYPE_BILINEAR);
		Image i = SwingFXUtils.toFXImage(op.filter(im, null), null);
		previewImage.setImage(i);
		mainApp.setFullImage(i);
		mainApp.setFilteredImage(i);

	}

	/**
	 * Handler that removes a image from the list.
	 */
	@FXML
	private void handleRemoveImage() {
		int selectedIndex = filesListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Paths.get(mainApp.getProjectPath(), "Fragments", filesListView.getItems().get(selectedIndex)).toFile()
					.delete();
			filesListView.getItems().remove(selectedIndex);
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No Selection");
			alert.setHeaderText("No Image Selected");
			alert.setContentText("Please select a image in the table to delete it");
			alert.showAndWait();
		}
	}

	/**
	 * Handler that changes to the image filter stage when called.
	 */
	@FXML
	private void nextScreen() {
		mainApp.showImageFilters();
	}

	/**
	 * Handler runs the OpenCV code to Stitch images toguether.
	 */
	@FXML
	private void launchStitcher() {

		new Thread(() -> {
			try {
				List<String> tempList = new ArrayList<>();
				StringBuilder tempString = new StringBuilder();
				mainApp.getFilesList()
						.forEach(x -> tempList.add(Paths.get(mainApp.getProjectPath(), "Fragments", x).toString()));
				for (String i : tempList)
					tempString.append(" " + i);
				Process stitcher = Runtime.getRuntime()
						.exec(this.getClass().getResource("/rsc/stitching/bin/Stitching.exe").getPath() + " "
								+ Paths.get(mainApp.getProjectPath(), "Full_Image", "Full_Image.png") + " "
								+ tempString);
				stitcher.waitFor();

				java.awt.Image full = new Opener()
						.openImage(Paths.get(mainApp.getProjectPath(), "Full_Image", "Full_Image.png").toString())
						.getImage();

				Platform.runLater(() -> {
					mainApp.setFullImage(SwingFXUtils.toFXImage((BufferedImage) full, null));
					this.previewImage.setImage(SwingFXUtils.toFXImage((BufferedImage) full, null));
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * Also, sets the Images. This is done here because when the method
	 * initialize is called, there is no reference to the mainapp.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		if (mainApp.getFullImage() != null) {
			previewImage.setImage(mainApp.getFullImage());
		}

		/*if (mainApp.getProjectPath() == null) {
			mainApp.createNewProject();
		}*/
		// TODO block actions if project has not been created.
		/*for (File i : Paths.get(mainApp.getProjectPath(), "Fragments").toFile().listFiles()) {
			mainApp.getFilesList().add(i.getName());
		}*/

		// Add observable list data to the table
		filesListView.setItems(mainApp.getFilesList());
	}

}
