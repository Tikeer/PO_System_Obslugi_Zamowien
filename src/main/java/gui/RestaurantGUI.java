package gui;

import manager.MenuManager;
import manager.OrderManager;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import pricing.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RestaurantGUI extends JFrame {

    private MenuManager menuManager;
    private OrderManager orderManager;


    private DefaultListModel<MenuItem> menuListModel;
    private JList<MenuItem> menuList;
    private JComboBox<MenuItem> posMenuComboBox;


    private DefaultListModel<OrderItem> currentBasketModel;
    private Order currentDraftOrder;


    private JTextArea historyTextArea;

    public RestaurantGUI(MenuManager menuManager, OrderManager orderManager) {
        this.menuManager = menuManager;
        this.orderManager = orderManager;

        setTitle("System Zarządzania Zamówieniami");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        menuListModel = new DefaultListModel<>();
        refreshMenuModels();

        currentBasketModel = new DefaultListModel<>();

        currentDraftOrder = new Order(999);


        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Menu (Zarządzanie)", createMenuPanel());
        tabbedPane.addTab("Nowe Zamówienie (Kasa)", createPosPanel());
        tabbedPane.addTab("Historia Zamówień", createHistoryPanel());


        tabbedPane.addChangeListener(e -> refreshHistory());

        add(tabbedPane);
    }


    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        menuList = new JList<>(menuListModel);
        panel.add(new JScrollPane(menuList), BorderLayout.CENTER);


        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dodaj nową pozycję"));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField categoryField = new JTextField();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nazwa:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Cena (zł):"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Kategoria:"));
        formPanel.add(categoryField);

        JButton addButton = new JButton("Dodaj do Menu");
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = categoryField.getText();

                MenuItem newItem = new MenuItem(id, name, price, category);
                menuManager.addItem(newItem);

                refreshMenuModels();


                idField.setText(""); nameField.setText("");
                priceField.setText(""); categoryField.setText("");

                JOptionPane.showMessageDialog(this, "Dodano pozycję!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Błąd formatu danych! Upewnij się, że ID i Cena są liczbami.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel(""));
        formPanel.add(addButton);

        panel.add(formPanel, BorderLayout.EAST);
        return panel;
    }


    private JPanel createPosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        posMenuComboBox = new JComboBox<>();
        updateComboBoxData();

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JTextField notesField = new JTextField(15);
        JButton addToBasketButton = new JButton("Dodaj do koszyka");

        selectionPanel.add(new JLabel("Produkt:"));
        selectionPanel.add(posMenuComboBox);
        selectionPanel.add(new JLabel("Ilość:"));
        selectionPanel.add(quantitySpinner);
        selectionPanel.add(new JLabel("Uwagi:"));
        selectionPanel.add(notesField);
        selectionPanel.add(addToBasketButton);

        panel.add(selectionPanel, BorderLayout.NORTH);


        JList<OrderItem> basketList = new JList<>(currentBasketModel);
        panel.add(new JScrollPane(basketList), BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JComboBox<String> pricingRulesCombo = new JComboBox<>(new String[]{
                "Standardowy", "Promocja Studencka", "Happy Hour"
        });

        JButton submitOrderButton = new JButton("Złóż zamówienie");

        addToBasketButton.addActionListener(e -> {
            MenuItem selectedItem = (MenuItem) posMenuComboBox.getSelectedItem();
            if (selectedItem != null) {
                int qty = (int) quantitySpinner.getValue();
                String notes = notesField.getText();
                OrderItem orderItem = new OrderItem(selectedItem, qty, notes);

                currentBasketModel.addElement(orderItem);
                currentDraftOrder.addItem(orderItem);

                notesField.setText("");
                quantitySpinner.setValue(1);
            }
        });

        submitOrderButton.addActionListener(e -> {
            if (currentBasketModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Koszyk jest pusty!");
                return;
            }


            PricingRule rule = new StandardPricing();
            String selectedRule = (String) pricingRulesCombo.getSelectedItem();
            if ("Promocja Studencka".equals(selectedRule)) rule = new StudentPromo();
            else if ("Happy Hour".equals(selectedRule)) rule = new HappyHour();


            Order finalOrder = orderManager.createOrder(rule);
            for (int i = 0; i < currentBasketModel.size(); i++) {
                finalOrder.addItem(currentBasketModel.get(i));
            }
            finalOrder.changeStatus(OrderStatus.COMPLETED);

            JOptionPane.showMessageDialog(this, "Zamówienie zrealizowane!\nDo zapłaty: " + finalOrder.getTotalPrice() + " zł");


            currentBasketModel.clear();
            currentDraftOrder = new Order(999);
            refreshHistory();
        });

        bottomPanel.add(new JLabel("Wybierz promocję:"));
        bottomPanel.add(pricingRulesCombo);
        bottomPanel.add(submitOrderButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }


    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        panel.add(new JScrollPane(historyTextArea), BorderLayout.CENTER);

        return panel;
    }

    private void refreshMenuModels() {
        menuListModel.clear();
        for (MenuItem item : menuManager.getAllItems()) {
            menuListModel.addElement(item);
        }
        updateComboBoxData();
    }

    private void updateComboBoxData() {
        if (posMenuComboBox != null) {
            posMenuComboBox.removeAllItems();
            for (MenuItem item : menuManager.getAllItems()) {
                posMenuComboBox.addItem(item);
            }
        }
    }

    private void refreshHistory() {
        if (historyTextArea == null) return;

        StringBuilder sb = new StringBuilder();
        List<Order> orders = orderManager.getAllOrders();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        for (Order order : orders) {

            String dateStr = sdf.format(new Date(order.getCreatedAt()));

            sb.append("ID Zamówienia: ").append(order.getOrderID())
                    .append(" | Data: ").append(dateStr)
                    .append(" | Status: ").append(order.getOrderStatus()).append("\n");

            for (OrderItem item : order.getItems()) {
                sb.append("   - ").append(item.toString()).append("\n");
            }
            sb.append("SUMA: ").append(order.getTotalPrice()).append(" zł\n");
            sb.append("---------------------------------------------------\n");
        }
        historyTextArea.setText(sb.toString());
    }
}