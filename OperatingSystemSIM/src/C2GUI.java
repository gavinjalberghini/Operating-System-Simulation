import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class C2GUI extends JFrame {

    //REAL TIME VISUALIZATIONS
    public JProgressBar processResOneBar = new JProgressBar();
    public JProgressBar processResTwoBar = new JProgressBar();
    public JProgressBar ramUtilizationBar = new JProgressBar();
    public JProgressBar vmUtilizationBar = new JProgressBar();

    //INPUT
    public JTextField cmdLine = new JTextField(30);

    //TEXT AREAS
    public JTextArea mainConsoleTextArea = new JTextArea();
    public JTextArea activeProcessTextArea = new JTextArea();

    //PANES (Boxes)
    public JScrollPane mainDisplayConsole = new JScrollPane(mainConsoleTextArea);
    public JScrollPane activeProcessDisplay = new JScrollPane(activeProcessTextArea);
    public JScrollPane cmdHist;

    //LABELS
    public JTextPane processResOnePercentUsageLabel = new JTextPane();
    public JTextPane processResTwoPercentUsageLabel = new JTextPane();
    public JTextPane ramPercentUsageLabel = new JTextPane();
    public JTextPane vmPercentUsageLabel = new JTextPane();

    //STYLE DOCS
    public StyledDocument processResOnePercentUsageLabelDoc = processResOnePercentUsageLabel.getStyledDocument();
    public StyledDocument processResTwoPercentUsageLabelDoc = processResTwoPercentUsageLabel.getStyledDocument();
    public StyledDocument ramPercentUsageLabelDoc = ramPercentUsageLabel.getStyledDocument();
    public StyledDocument vmPercentUsageLabelDoc = vmPercentUsageLabel.getStyledDocument();
    public SimpleAttributeSet center = new SimpleAttributeSet();

    //BORDER
    public Border emptyBorder = BorderFactory.createEmptyBorder();
    public Border bord;

    //GUI POST INFORMATION
    public String osSnapshot = "";
    public String activeProcesses = "";
    public String cmdInput = "Pass()";

    //GUI WINDOW
    JFrame window = new JFrame("Control Terminal");

    public int processResOnePercentage = 0;
    public int processResTwoPercentage = 0;
    public int ramPercentage = 0;
    public int vmPercentage = 0;

    public C2GUI(String title) {
        //super(title);
        setUndecorated(true);
        initUI();
    }

    public void initUI() {

        window.setVisible(true);
        // Sets the size of the window to max
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JTextArea cmdHistory = new JTextArea();;
        cmdHistory.setBackground(Color.GRAY);
        cmdHistory.setOpaque(true);
        cmdHist  = new JScrollPane(cmdHistory);

        JButton enterButton = new JButton("Enter");
        enterButton.setBackground(Color.GRAY);
        enterButton.setOpaque(true);
        enterButton.setBorderPainted(false);

        cmdLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmdInput = cmdLine.getText() + "\n";
                System.out.println(cmdInput);
                cmdHistory.append(cmdInput);
                cmdLine.setText("");
            }

        });

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmdInput = cmdLine.getText() + "\n";
                System.out.println(cmdInput);
                cmdHistory.append(cmdInput);
                cmdLine.setText("");
            }
        });

        int top = 10;
        int left = 0;
        int bottom = 0;
        int right = 40;
        gbc.insets = new Insets(top, left, bottom, right);

        // Adds the progress bar to the layout
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .1;
        gbc.weighty = .1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        processResOnePercentUsageLabel.setBackground(Color.GRAY);
        processResOnePercentUsageLabel.setOpaque(true);
        processResOnePercentUsageLabel.setFont(processResOnePercentUsageLabel.getFont().deriveFont(14f));
        processResOnePercentUsageLabel.setPreferredSize(new Dimension(processResOnePercentUsageLabel.getWidth(),processResOnePercentUsageLabel.getHeight() / 2));
        panel.add(processResOnePercentUsageLabel, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .1;
        gbc.weighty = .1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        processResOneBar.setSize(50, 100);
        processResOneBar.setVisible(true);
        processResOneBar.setPreferredSize(new Dimension(processResOneBar.getWidth(),processResOneBar.getHeight() / 10));
        panel.add(processResOneBar, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .1;
        gbc.weighty = .1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        processResOnePercentUsageLabel.setFont(processResOnePercentUsageLabel.getFont().deriveFont(14f));
        processResTwoPercentUsageLabel.setBackground(Color.GRAY);
        processResTwoPercentUsageLabel.setOpaque(true);
        processResTwoPercentUsageLabel.setPreferredSize(new Dimension(processResTwoPercentUsageLabel.getWidth(),processResTwoPercentUsageLabel.getHeight()));
        panel.add(processResTwoPercentUsageLabel, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .1;
        gbc.weighty = .1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        processResTwoBar.setSize(50, 100);
        processResTwoBar.setVisible(true);
        processResTwoBar.setPreferredSize(new Dimension(processResTwoBar.getWidth(),processResTwoBar.getHeight()));
        panel.add(processResTwoBar, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .1;
        gbc.weighty = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        ramPercentUsageLabel.setFont(processResOnePercentUsageLabel.getFont().deriveFont(14f));
        ramPercentUsageLabel.setBackground(Color.GRAY);
        ramPercentUsageLabel.setOpaque(true);
        ramPercentUsageLabel.setPreferredSize(new Dimension(ramPercentUsageLabel.getWidth(),ramPercentUsageLabel.getHeight()));
        panel.add(ramPercentUsageLabel, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .1;
        gbc.weighty = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        ramUtilizationBar.setSize(50, 100);
        ramUtilizationBar.setVisible(true);
        ramUtilizationBar.setPreferredSize(new Dimension(ramUtilizationBar.getWidth(), ramUtilizationBar.getHeight()));
        panel.add(ramUtilizationBar, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .1;
        gbc.weighty = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        processResOnePercentUsageLabel.setFont(processResOnePercentUsageLabel.getFont().deriveFont(14f));
        vmPercentUsageLabel.setBackground(Color.GRAY);
        vmPercentUsageLabel.setOpaque(true);
        vmPercentUsageLabel.setPreferredSize(new Dimension(vmPercentUsageLabel.getWidth(),vmPercentUsageLabel.getHeight()));
        panel.add(vmPercentUsageLabel, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .1;
        gbc.weighty = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 7;
        vmUtilizationBar.setSize(50, 100);
        vmUtilizationBar.setVisible(true);
        vmUtilizationBar.setPreferredSize(new Dimension(vmUtilizationBar.getWidth(),vmUtilizationBar.getHeight()));
        panel.add(vmUtilizationBar, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .04;
        gbc.weighty = 25;
        gbc.gridheight = 2;
        gbc.gridwidth = 3;
        gbc.gridx = 5;
        gbc.gridy = 0;
        activeProcessDisplay.setPreferredSize(new Dimension(activeProcessTextArea.getWidth(), activeProcessTextArea.getHeight()));
        activeProcessDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bord = BorderFactory.createTitledBorder(emptyBorder, "CPU", TitledBorder.TOP, TitledBorder.CENTER);
        activeProcessDisplay.setBorder(bord);
        panel.add(activeProcessDisplay, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .04;
        gbc.weighty = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 3;
        gbc.gridx = 5;
        gbc.gridy = 5;
        cmdHist.setPreferredSize(new Dimension(cmdHistory.getWidth(), cmdHistory.getHeight()));
        cmdHist.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bord = BorderFactory.createTitledBorder(emptyBorder, "Command History", TitledBorder.TOP, TitledBorder.CENTER);
        cmdHist.setBorder(bord);
        panel.add(cmdHist, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .6;
        gbc.gridwidth = 4;
        gbc.gridheight = 8;
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainDisplayConsole.setPreferredSize(new Dimension(mainDisplayConsole.getWidth(), mainDisplayConsole.getHeight()));
        mainDisplayConsole.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bord = BorderFactory.createTitledBorder(emptyBorder, "Main Display", TitledBorder.TOP, TitledBorder.CENTER);
        mainDisplayConsole.setBorder(bord);
        panel.add(mainDisplayConsole, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .001;
        gbc.weighty = .001;
        gbc.gridx = 5;
        gbc.gridy = 7;
        panel.add(cmdLine, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .001;
        gbc.weighty = .01;
        gbc.gridheight = 1;
        gbc.gridx = 8;
        gbc.gridy = 7;
        panel.add(enterButton, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = .04;
        gbc.weighty = 30;
        gbc.gridheight = 3;
        gbc.gridwidth = 3;
        gbc.gridx = 5;
        gbc.gridy = 2;
        JScrollPane newBox = new JScrollPane();
        newBox.setBackground(Color.white);

        newBox.setPreferredSize(new Dimension(cmdHistory.getWidth(), cmdHistory.getHeight()));
        newBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bord = BorderFactory.createTitledBorder(emptyBorder, "New Box", TitledBorder.TOP, TitledBorder.CENTER);
        newBox.setBorder(bord);
        panel.add(newBox, gbc);
        window.add(panel);
    }

    public String getCommandLineString(){
        return cmdInput;
    }

    public void setProcessResOnePercentage(int num) {
        processResOnePercentage = num;
        processResOneBar.setValue(processResOnePercentage);
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        processResOnePercentUsageLabelDoc.setParagraphAttributes(0, processResOnePercentUsageLabelDoc.getLength(), center, false);
        processResOnePercentUsageLabel.setText("\n\n\n0% -- Process Resource One (" + processResOnePercentage + "%) -- 100%");
    }

    public void setProcessResTwoPercentage(int num) {
        processResTwoPercentage = num;
        processResTwoBar.setValue(processResTwoPercentage);
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        processResTwoPercentUsageLabelDoc.setParagraphAttributes(0, processResTwoPercentUsageLabelDoc.getLength(), center, false);
        processResTwoPercentUsageLabel.setText("\n\n\n0% -- Process Resource Two (" + processResTwoPercentage + "%) -- 100%");
    }

    public void setRamPercentage(int num) {
        ramPercentage = num;
        ramUtilizationBar.setValue(ramPercentage);
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        ramPercentUsageLabelDoc.setParagraphAttributes(0, ramPercentUsageLabelDoc.getLength(), center, false);
        ramPercentUsageLabel.setText("\n\n\n0% -- Random Access Memory (" + ramPercentage + "%) -- 100%");
    }

    public void setVmPercentage(int num) {
        vmPercentage = num;
        vmUtilizationBar.setValue(vmPercentage);
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        vmPercentUsageLabelDoc.setParagraphAttributes(0, vmPercentUsageLabelDoc.getLength(), center, false);
        vmPercentUsageLabel.setText("\n\n\n0% -- Virtual Memory (" + vmPercentage + "%) -- 100%");
    }

    public void setOsSnapshot(String str) {
        osSnapshot = str;
        mainConsoleTextArea.setText(osSnapshot + "\n");
    }

    public void setActiveProcesses(String str) {
        activeProcesses = str;
        activeProcessTextArea.setText(activeProcesses + "\n");
    }

    public void commandAquired(){
        cmdInput = "Pass()";
    }

    public void update(int num1, int num2, int num3, int num4, String processesInOS, String processesInCPU) {
        setProcessResOnePercentage(num1);
        setProcessResTwoPercentage(num2);
        setRamPercentage(num3);
        setVmPercentage(num4);
        setOsSnapshot(processesInOS);
        setActiveProcesses(processesInCPU);

    }
}
