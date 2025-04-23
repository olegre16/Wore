import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ForeignWorkerManager {
    // Модель данных для хранения информации о работниках
    static class Worker {
        String name;
        String name1;
        String name2;
        String passportNumber;
        String country;
        LocalDate passportExpiry;
        LocalDate employmentDate;

        public Worker(String name, String name1, String name2,String passportNumber, String country, LocalDate passportExpiry, LocalDate employmentDate) {
            this.name = name;
            this.name1 = name1;
            this.name2 = name2;
            this.passportNumber = passportNumber;
            this.country = country;
            this.passportExpiry = passportExpiry;
            this.employmentDate = employmentDate;
        }

        @Override
        public String toString() {
            return "Имя: " + name + ", Фамилия: " + name1 + ", Отчество: " + name2 + ", Паспорт: " + passportNumber + ", Страна: " + country +
                    ", Паспорт истекает: " + passportExpiry + ", Дата найма: " + employmentDate;
        }
    }

    private ArrayList<Worker> workers = new ArrayList<>();

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Учёт иностранных работников");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Главная панель
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Панель для добавления работника
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2));

        JTextField nameField = new JTextField();
        JTextField name2Field = new JTextField();
        JTextField name1Field = new JTextField();
        JTextField passportField = new JTextField();
        JTextField countryField = new JTextField();
        JTextField expiryField = new JTextField();
        JTextField employmentField = new JTextField();

        inputPanel.add(new JLabel(" Имя:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel(" Фамилия:"));
        inputPanel.add(name1Field);
        inputPanel.add(new JLabel(" Отчество (при наличии):"));
        inputPanel.add(name2Field);
        inputPanel.add(new JLabel(" Номер паспорта:"));
        inputPanel.add(passportField);
        inputPanel.add(new JLabel(" Страна:"));
        inputPanel.add(countryField);
        inputPanel.add(new JLabel(" Дата окончания паспорта (YYYY-MM-DD):"));
        inputPanel.add(expiryField);
        inputPanel.add(new JLabel(" Дата найма (YYYY-MM-DD):"));
        inputPanel.add(employmentField);
// как сместить кнопку?
        //как сместить кнопку????????

        JButton addButton = new JButton("Добавить работника");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    String name1 = name1Field.getText();
                    String name2 = name2Field.getText();
                    String passportNumber = passportField.getText();
                    String country = countryField.getText();
                    LocalDate passportExpiry = LocalDate.parse(expiryField.getText(), DateTimeFormatter.ISO_DATE);
                    LocalDate employmentDate = LocalDate.parse(employmentField.getText(), DateTimeFormatter.ISO_DATE);

                    if (passportExpiry.isBefore(LocalDate.now())) {
                        JOptionPane.showMessageDialog(frame, "Ошибка: Паспорт просрочен!");
                        return;
                    }

                    Worker worker = new Worker(name, name1, name2, passportNumber, country, passportExpiry, employmentDate);
                    workers.add(worker);
                    updateWorkerList(mainPanel);
                    clearFields(nameField, name1Field, name2Field, passportField, countryField, expiryField, employmentField);
                    JOptionPane.showMessageDialog(frame, "Работник успешно добавлен!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка ввода данных. Проверьте формат дат.");
                }
            }
        });

        inputPanel.add(addButton);

        // Панель для отображения списка работников
        JTextArea workerListArea = new JTextArea();
        workerListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(workerListArea);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel();
        JButton checkPassportsButton = new JButton("Проверить просроченные паспорта");
        JButton exportButton = new JButton("Экспорт отчёта");

        checkPassportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder expiredPassports = new StringBuilder("Просроченные паспорта:\n");
                boolean found = false;
                for (Worker worker : workers) {
                    if (worker.passportExpiry.isBefore(LocalDate.now())) {
                        expiredPassports.append(worker.toString()).append("\n");
                        found = true;
                    }
                }
                if (!found) {
                    expiredPassports.append("Все паспорта действительны.");
                }
                JOptionPane.showMessageDialog(frame, expiredPassports.toString());
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (FileWriter writer = new FileWriter("workers_report.txt")) {
                    for (Worker worker : workers) {
                        writer.write(worker.toString() + "\n");
                    }
                    JOptionPane.showMessageDialog(frame, "Отчёт успешно экспортирован в workers_report.txt");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка при экспорте отчёта.");
                }
            }
        });

        buttonPanel.add(checkPassportsButton);
        buttonPanel.add(exportButton);

        // Обновление главной панели
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void updateWorkerList(JPanel mainPanel) {
        StringBuilder list = new StringBuilder("Список работников:\n");
        for (Worker worker : workers) {
            list.append(worker.toString()).append("\n");
        }
        JTextArea workerListArea = new JTextArea(list.toString());
        workerListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(workerListArea);
        mainPanel.remove(1); // Удаляем старую панель
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ForeignWorkerManager manager = new ForeignWorkerManager();
            manager.createAndShowGUI();
        });
    }
}