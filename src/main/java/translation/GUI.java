package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {


            Translator translator = new JSONTranslator();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();



            JPanel countryPanel = new JPanel();

            String[] items = new String[translator.getCountryCodes().size()];

            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                items[i++] = countryCodeConverter.fromCountryCode(countryCode);
            }

            // create the JList with the array of strings and set it to allow multiple
            // items to be selected at once.
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane);

            //

            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            // create combobox, add country codes into it, and add it to our panel
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String LanguageCodes : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageCodeConverter.fromLanguageCode(LanguageCodes));
            }
            languagePanel.add(languageComboBox);

            //

            JPanel resultPanel = new JPanel();
            JLabel resultLabelText = new JLabel("Translation:");
            resultPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            resultPanel.add(resultLabel);

            Runnable updateTranslation = () -> {
                String selectedLanguageCode = languageCodeConverter.fromLanguage((String) languageComboBox.getSelectedItem());
                String selectedCountryCode = countryCodeConverter.fromCountry(list.getSelectedValue());

                if (selectedCountryCode == null || selectedLanguageCode == null) {
                    resultLabel.setText(" ");
                    return;
                }

                String result = translator.translate(selectedCountryCode.toLowerCase(), selectedLanguageCode);
                if (result == null) {
                    result = " ";
                }
                resultLabel.setText(result);
            };

            // Fire translation whenever language changes
            languageComboBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateTranslation.run();
                }
            });

            // Fire translation whenever country changes
            list.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    updateTranslation.run();
                }
            });


            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(resultPanel);
            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
