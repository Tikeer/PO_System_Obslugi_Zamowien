package gui;

import manager.DataManager;
import manager.MenuManager;
import manager.OrderManager;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import pricing.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.channels.SelectableChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RestaurantGUI extends JFrame {

    private MenuManager menuManager;
    private OrderManager orderManager;
    private DataManager dataManager;


    private DishOfTheDay activeDishOfTheDay = new DishOfTheDay();
    private ComboSet activeComboSet = new ComboSet();
    private HappyHour activeHappyHour = new HappyHour();


    private PricingRule selectedDynamicRule = null;


    private DefaultListModel<MenuItem> menuListModel;
    private JList<MenuItem> menuList;
    private JComboBox<MenuItem> posMenuComboBox;
    private DefaultListModel<OrderItem> currentBasketModel;
    private DefaultListModel<Order> kitchenOrdersModel;


    private JTextArea historyTextArea;
    private JLabel lblTotalRevenue, lblAvgOrderValue, lblCompletedCount, lblCancelledCount;

    public RestaurantGUI(MenuManager menuManager, OrderManager orderManager,DataManager dataManager) {
        this.menuManager = menuManager;
        this.orderManager = orderManager;
        this.dataManager = dataManager;

        setTitle("System Zarządzania Restauracją - Panel Główny");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuListModel = new DefaultListModel<>();
        currentBasketModel = new DefaultListModel<>();
        kitchenOrdersModel = new DefaultListModel<>();

        refreshMenuModels();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("Kasa", createPosPanel());
        tabbedPane.addTab("Panel Kuchenny", createKitchenPanel());
        tabbedPane.addTab("Zarządzanie Menu", createMenuManagementPanel());
        tabbedPane.addTab("Statystyki i Historia", createStatsAndHistoryPanel());

        final int[] previousTab = {0};

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();

            if(selectedIndex == 2){
                JPasswordField passwordField = new JPasswordField(10);
                int action = JOptionPane.showConfirmDialog(this,passwordField,"Podaj PIN menedżera:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                String inputPin = new String(passwordField.getPassword());

                if (action == JOptionPane.OK_OPTION && inputPin.equals("1234")){
                    previousTab[0] = selectedIndex;
                }else{
                    JOptionPane.showMessageDialog(this,"Błędny PIN! Odmowa dostępu.", "Brak uprawnień", JOptionPane.ERROR_MESSAGE);
                    SwingUtilities.invokeLater(() -> tabbedPane.setSelectedIndex(previousTab[0]));
                    return;
                }
            }else{
                previousTab[0] = selectedIndex;
            }

            refreshKitchen();
            refreshHistoryAndStats();
        });

        add(tabbedPane);

        refreshKitchen();
        refreshHistoryAndStats();
    }

    private JPanel createPosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        JPanel selectionPanel = new JPanel(new BorderLayout(10, 10));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Wybór produktów"));

        posMenuComboBox = new JComboBox<>();
        updateComboBoxData();

        JComboBox<String> sizeBox = new JComboBox<>(new String[]{"Standard","Mała","Średnia","Duża"});
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JTextField notesField = new JTextField(15);
        JButton addToBasketButton = new JButton("Dodaj do koszyka ➕");
        addToBasketButton.setBackground(new Color(230, 245, 230));

        JPanel inputsPanel = new JPanel(new GridLayout(2, 4, 10, 2));

        inputsPanel.add(new JLabel("Produkt:"));
        inputsPanel.add(new JLabel("Rozmiar:"));
        inputsPanel.add(new JLabel("Ilość:"));
        inputsPanel.add(new JLabel("Uwagi:"));

        inputsPanel.add(posMenuComboBox);
        inputsPanel.add(sizeBox);
        inputsPanel.add(quantitySpinner);
        inputsPanel.add(notesField);

        selectionPanel.add(inputsPanel, BorderLayout.CENTER);
        selectionPanel.add(addToBasketButton, BorderLayout.EAST);

        panel.add(selectionPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Aktualna zawartość koszyka"));
        JList<OrderItem> basketList = new JList<>(currentBasketModel);
        basketList.setFont(new Font("Monospaced", Font.PLAIN, 13));
        centerPanel.add(new JScrollPane(basketList), BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);


        JPanel southContainer = new JPanel(new BorderLayout(10, 10));

        JPanel promoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        promoPanel.setBorder(BorderFactory.createTitledBorder("Promocje Losowe"));

        JButton randomizeDishButton = new JButton("Losuj Danie Dnia");
        JLabel dishInfoLabel = new JLabel("Aktywne danie dnia: Brak");
        dishInfoLabel.setForeground(Color.BLUE);

        JButton randomizeComboButton = new JButton("Losuj Zestaw Combo");
        JLabel comboInfoLabel = new JLabel("Aktywne combo: Brak");
        comboInfoLabel.setForeground(new Color(128, 0, 128));

        JLabel happyHourLabel = new JLabel("Happy Hour dzisiaj o godzinie: 15:00");
        happyHourLabel.setForeground(new Color(210, 105, 30));

        randomizeDishButton.addActionListener(e -> {
            List<MenuItem> drawnItems = menuManager.getRandomItems(1);
            if (!drawnItems.isEmpty()) {
                activeDishOfTheDay.refreshDailyItems(drawnItems);
                dishInfoLabel.setText("Aktywne danie: " + activeDishOfTheDay.getDescription() + " [ZNIŻKA]");
                comboInfoLabel.setText("Aktywne combo: Brak");

                selectedDynamicRule = activeDishOfTheDay;
                MenuItem dish = drawnItems.get(0);
                currentBasketModel.addElement(new OrderItem(dish, 1, "Wylosowane Danie Dnia"));
            } else {
                JOptionPane.showMessageDialog(this, "Baza menu jest pusta!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        randomizeComboButton.addActionListener(e -> {
            List<MenuItem> drawnItems = menuManager.getRandomItems(2);
            if (drawnItems.size() == 2) {
                activeComboSet.randomizeCombo(drawnItems);
                comboInfoLabel.setText("Aktywne combo: " + activeComboSet.getRuleName() + " [ZNIŻKA]");
                dishInfoLabel.setText("Aktywne danie dnia: Brak");

                selectedDynamicRule = activeComboSet;
                for (MenuItem item : drawnItems) {
                    currentBasketModel.addElement(new OrderItem(item, 1, "Składnik Zestawu Combo"));
                }
            } else {
                JOptionPane.showMessageDialog(this, "W menu muszą być minimum 2 produkty!", "Błąd", JOptionPane.WARNING_MESSAGE);
            }
        });

        promoPanel.add(randomizeDishButton);   promoPanel.add(dishInfoLabel);
        promoPanel.add(randomizeComboButton);  promoPanel.add(comboInfoLabel);
        promoPanel.add(new JLabel(""));        promoPanel.add(happyHourLabel);

        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        JButton removeFromBasketButton = new JButton("Usuń zaznaczone ");
        removeFromBasketButton.setBackground(new Color(255, 204, 204));

        JComboBox<String> pricingRulesCombo = new JComboBox<>(new String[]{"Bez promocji", "Promocja Studencka"});
        JButton submitOrderButton = new JButton("Przekaż do kuchni ");
        submitOrderButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        submitOrderButton.setBackground(new Color(204, 230, 255));

        removeFromBasketButton.addActionListener(e -> {
            int selectedIndex = basketList.getSelectedIndex();
            if (selectedIndex != -1) {
                currentBasketModel.remove(selectedIndex);
            }
        });

        checkoutPanel.add(removeFromBasketButton);
        checkoutPanel.add(new JSeparator(SwingConstants.VERTICAL));
        checkoutPanel.add(new JLabel("Typ promocji:"));
        checkoutPanel.add(pricingRulesCombo);
        checkoutPanel.add(submitOrderButton);

        southContainer.add(promoPanel, BorderLayout.NORTH);
        southContainer.add(checkoutPanel, BorderLayout.SOUTH);
        panel.add(southContainer, BorderLayout.SOUTH);

        addToBasketButton.addActionListener(e -> {
            MenuItem selectedItem = (MenuItem) posMenuComboBox.getSelectedItem();
            if (selectedItem != null) {
                String size = (String) sizeBox.getSelectedItem();
                String rawNotes = notesField.getText();
                String finalNotes = size.equals("Standard") ? rawNotes : "Rozmiar: " + size + (rawNotes.isEmpty() ? "" : ", " + rawNotes);

                currentBasketModel.addElement(new OrderItem(selectedItem, (int) quantitySpinner.getValue(), finalNotes));
                notesField.setText("");
                quantitySpinner.setValue(1);
                sizeBox.setSelectedIndex(0);
            }
        });

        submitOrderButton.addActionListener(e -> {
            if (currentBasketModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Koszyk jest pusty!");
                return;
            }

            PricingRule finalRule = new StandardPricing();
            String manualSelection = (String) pricingRulesCombo.getSelectedItem();

            if ("Promocja Studencka".equals(manualSelection)) {
                finalRule = new StudentPromo();
            } else {
                if (selectedDynamicRule != null) {
                    finalRule = selectedDynamicRule;
                } else if (activeHappyHour.isHappyHourNow()) {
                    finalRule = activeHappyHour;
                }
            }


            Order finalOrder = orderManager.createOrder(finalRule);
            for (int i = 0; i < currentBasketModel.size(); i++) {
                finalOrder.addItem(currentBasketModel.get(i));
            }

            finalOrder.changeStatus(OrderStatus.PENDING);


            dataManager.saveOrder(orderManager.getAllOrders());


            JOptionPane.showMessageDialog(this, "Zamówienie wysłane do kuchni!\nZastosowano regułę: "
                    + finalRule.getRuleName() + "\nWartość: " + String.format("%.2f", finalOrder.getTotalPrice()) + " zł");

            currentBasketModel.clear();
            selectedDynamicRule = null;
            dishInfoLabel.setText("Aktywne danie dnia: Brak");
            comboInfoLabel.setText("Aktywne combo: Brak");
            pricingRulesCombo.setSelectedIndex(0);

            refreshKitchen();
        });

        return panel;
    }

    private JPanel createKitchenPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JList<Order> kitchenList = new JList<>(kitchenOrdersModel);
        kitchenList.setFont(new Font("Monospaced", Font.PLAIN, 13));

        panel.add(new JScrollPane(kitchenList), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnComplete = new JButton("Wydaj zamówienie ");
        JButton btnCancel = new JButton("Anuluj zamówienie ");

        btnComplete.setBackground(new Color(204, 255, 204));
        btnCancel.setBackground(new Color(255, 204, 204));

        btnComplete.addActionListener(e -> {
            Order selectedOrder = kitchenList.getSelectedValue();
            if (selectedOrder != null) {
                selectedOrder.changeStatus(OrderStatus.COMPLETED);
                dataManager.saveOrder(orderManager.getAllOrders());
                refreshKitchen();
            }
        });

        btnCancel.addActionListener(e -> {
            Order selectedOrder = kitchenList.getSelectedValue();
            if (selectedOrder != null) {
                selectedOrder.changeStatus(OrderStatus.CANCELLED);
                dataManager.saveOrder(orderManager.getAllOrders());
                refreshKitchen();
            }
        });

        controlPanel.add(btnComplete);
        controlPanel.add(btnCancel);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMenuManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        menuList = new JList<>(menuListModel);
        panel.add(new JScrollPane(menuList), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Zarządzanie menu"));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox categoryBox = new JComboBox<>(new String[]{"Główne","Napoje", "Przystawki", "Desery"});


        formPanel.add(new JLabel("Nazwa:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Cena:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Kategoria:"));
        formPanel.add(categoryBox);

        JButton addButton = new JButton("Dodaj");
        JButton deleteButton = new JButton("Usuń️");
        deleteButton.setBackground(new Color(255, 230, 230));

        addButton.addActionListener(e -> {
            try {
                int newId = 1;
                for(MenuItem item : menuManager.getAllItems()){
                    if(item.getID() >= newId) {
                        newId = item.getID() + 1;
                    }
                }

                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String category = (String) categoryBox.getSelectedItem();

                menuManager.addItem(new MenuItem(newId, name, price, category));
                dataManager.saveMenu(menuManager.getAllItems());
                refreshMenuModels();

                nameField.setText("");
                priceField.setText("");
                categoryBox.setSelectedIndex(0);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Błędny format danych!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            MenuItem selectedItem = menuList.getSelectedValue();
            if (selectedItem != null) {
                menuManager.removeItem(selectedItem.getID());
                dataManager.saveMenu(menuManager.getAllItems());
                refreshMenuModels();
            }
        });

        formPanel.add(deleteButton);
        formPanel.add(addButton);
        panel.add(formPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatsAndHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel statsDashboard = new JPanel(new GridLayout(1, 4, 10, 10));
        statsDashboard.setBorder(BorderFactory.createTitledBorder(null, "DASHBOARD", TitledBorder.CENTER, TitledBorder.TOP));

        lblTotalRevenue = new JLabel("0.00 zł", SwingConstants.CENTER);
        lblTotalRevenue.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalRevenue.setForeground(new Color(0, 128, 0));
        JPanel p1 = new JPanel(new BorderLayout()); p1.add(new JLabel("Przychód", SwingConstants.CENTER), BorderLayout.NORTH); p1.add(lblTotalRevenue, BorderLayout.CENTER);

        lblAvgOrderValue = new JLabel("0.00 zł", SwingConstants.CENTER);
        lblAvgOrderValue.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel p2 = new JPanel(new BorderLayout()); p2.add(new JLabel("Średnie Zamówienie", SwingConstants.CENTER), BorderLayout.NORTH); p2.add(lblAvgOrderValue, BorderLayout.CENTER);

        lblCompletedCount = new JLabel("0", SwingConstants.CENTER);
        lblCompletedCount.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel p3 = new JPanel(new BorderLayout()); p3.add(new JLabel("Wydane", SwingConstants.CENTER), BorderLayout.NORTH); p3.add(lblCompletedCount, BorderLayout.CENTER);

        lblCancelledCount = new JLabel("0", SwingConstants.CENTER);
        lblCancelledCount.setFont(new Font("Arial", Font.BOLD, 18));
        lblCancelledCount.setForeground(Color.RED);
        JPanel p4 = new JPanel(new BorderLayout()); p4.add(new JLabel("Anulowane", SwingConstants.CENTER), BorderLayout.NORTH); p4.add(lblCancelledCount, BorderLayout.CENTER);

        statsDashboard.add(p1); statsDashboard.add(p2); statsDashboard.add(p3); statsDashboard.add(p4);
        panel.add(statsDashboard, BorderLayout.NORTH);

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
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

    private void refreshKitchen() {
        kitchenOrdersModel.clear();
        for (Order o : orderManager.getAllOrders()) {
            if (o.getOrderStatus() == OrderStatus.PENDING) {
                kitchenOrdersModel.addElement(o);
            }
        }
    }

    private void refreshHistoryAndStats() {
        if (historyTextArea == null) return;

        StringBuilder sb = new StringBuilder();
        List<Order> allOrders = orderManager.getAllOrders();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        double totalRevenue = 0;
        int completedCount = 0;
        int cancelledCount = 0;

        for (Order order : allOrders) {
            if (order.getOrderStatus() == OrderStatus.COMPLETED) {
                totalRevenue += order.getTotalPrice();
                completedCount++;
            } else if (order.getOrderStatus() == OrderStatus.CANCELLED) {
                cancelledCount++;
            }

            String dateStr = sdf.format(new Date(order.getCreatedAt()));
            sb.append("ZAMÓWIENIE ID: ").append(order.getOrderID())
                    .append(" | Data: ").append(dateStr)
                    .append(" | STATUS: ").append(order.getOrderStatus()).append("\n");

            for (OrderItem item : order.getItems()) {
                sb.append("   ↳ ").append(item.toString()).append("\n");
            }
            sb.append("   WARTOŚĆ: ").append(String.format("%.2f", order.getTotalPrice())).append(" zł\n");
            sb.append("----------------------------------------------------------------------\n");
        }

        lblTotalRevenue.setText(String.format("%.2f", totalRevenue) + " zł");
        lblCompletedCount.setText(String.valueOf(completedCount));
        lblCancelledCount.setText(String.valueOf(cancelledCount));

        double avg = (completedCount > 0) ? (totalRevenue / completedCount) : 0.0;
        lblAvgOrderValue.setText(String.format("%.2f", avg) + " zł");

        historyTextArea.setText(sb.toString());
    }
}