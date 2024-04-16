import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.*;


public class ProductForm3 extends JFrame {
    private String MONGO_URI = "mongodb://localhost:27017";
    private String DATABASE = "test";
    private String COLLECTION = "products3";
    private JTextField idField, nameField, priceField, descriptionField;

    public ProductForm3() {
        setTitle("Add Product");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Price:"));
        priceField = new JTextField();
        panel.add(priceField);

        panel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        panel.add(addButton);


        JButton readButton = new JButton("Read");
        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readProduct();
            }
        });
        panel.add(readButton);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        panel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });
        panel.add(deleteButton);

        add(panel);
        setVisible(true);
    }

    private void addProduct() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String description = descriptionField.getText();

            Document document = new Document()
                    .append("name", name)
                    .append("price", price)
                    .append("description", description);

            collection.insertOne(document);
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            String id = idField.getText();
            ObjectId objectId = new ObjectId(id);
            Bson filter = eq("_id", objectId);
            Document document = collection.findOneAndDelete(filter);
            System.out.println(document);

            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readProduct() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)){
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            String id = idField.getText();
            ObjectId objectId = new ObjectId(id);
            Bson filter = eq("_id", objectId);
            Document document = collection.find(filter).first();
            System.out.println(document);

            JOptionPane.showConfirmDialog(this, document);
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductForm3();
            }
        });
    }
}
