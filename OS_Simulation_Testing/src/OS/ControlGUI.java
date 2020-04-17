package OS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ControlGUI {
    private JButton enterButton;
    private JProgressBar processResourceOneBar;
    private JProgressBar processResourceTwoBar;
    private JProgressBar ramBar;
    private JProgressBar vmBar;
    private JTextArea processResourceOneLabel;
    private JTextArea processResourceTwoLabel;
    private JTextArea ramLabel;
    private JTextArea vmLabel;
    private JTextArea cpuDisplayConsole;
    private JTextArea commandHistoryConsole;
    private JTextField commandLine;
    private JScrollPane processLookUpConsole;
    private JScrollPane mainDisplayConsole;
    private JScrollPane eventConsole;
    private JTextArea processLookUpTextArea;
    private JTextArea mainDisplayConsoleTextArea;
    private JTextArea eventConsoleTextArea;
    private JPanel panel;

    //GUI POST INFORMATION
    public String osSnapshot = "";
    public String activeProcesses = "";
    public String cmdInput = "Pass()";
    public String processInfo = "";
    public String event = "";

    //GUI WINDOW
    JFrame window = new JFrame("Control Terminal");

    public int processResOnePercentage = 0;
    public int processResTwoPercentage = 0;
    public int ramPercentage = 0;
    public int vmPercentage = 0;

    public ControlGUI() {
        commandLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmdInput = commandLine.getText() + "\n";
                System.out.println(cmdInput);
                commandHistoryConsole.append(cmdInput);
                commandLine.setText("");
            }

        });

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmdInput = commandLine.getText() + "\n";
                System.out.println(cmdInput);
                commandHistoryConsole.append(cmdInput);
                commandLine.setText("");
            }
        });

        window.setVisible(true);
        // Sets the size of the window to max
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainDisplayConsole.setPreferredSize(new Dimension(mainDisplayConsole.getWidth(), mainDisplayConsole.getHeight()));
        processLookUpConsole.setPreferredSize(new Dimension(processLookUpConsole.getWidth(), processLookUpConsole.getHeight()));
        eventConsole.setPreferredSize(new Dimension(eventConsole.getWidth(), eventConsole.getHeight()));
        cpuDisplayConsole.setPreferredSize(new Dimension(cpuDisplayConsole.getWidth(), cpuDisplayConsole.getHeight()));


        panel.getAccessibleContext();
        window.add(panel);
    }

    public void showGUI(){
        window.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public String getCommandLineString(){
        return cmdInput;
    }

    public void setProcessResOnePercentage(int num) {
        processResOnePercentage = num;
        processResourceOneBar.setValue(processResOnePercentage);
        processResourceOneLabel.setText("\n0% -- Process Resource One (" + processResOnePercentage + "%) -- 100%");
    }

    public void setProcessResTwoPercentage(int num) {
        processResTwoPercentage = num;
        processResourceTwoBar.setValue(processResTwoPercentage);
        processResourceTwoLabel.setText("\n0% -- Process Resource Two (" + processResTwoPercentage + "%) -- 100%");
    }

    public void setRamPercentage(int num) {
        ramPercentage = num;
        ramBar.setValue(ramPercentage);
        ramLabel.setText("\n0% -- Random Access Memory (" + ramPercentage + "%) -- 100%");
    }

    public void setVmPercentage(int num) {
        vmPercentage = num;
        vmBar.setValue(vmPercentage);
        vmLabel.setText("\n0% -- Virtual Memory (" + vmPercentage + "%) -- 100%");
    }

    public void setOsSnapshot(String str) {
        osSnapshot = str;
        mainDisplayConsoleTextArea.setText(osSnapshot + "\n");
    }

    public void setActiveProcesses(String str) {
        activeProcesses = str;
        cpuDisplayConsole.setText(activeProcesses + "\n");
    }

    public void setProcessLookUp(String str) {
        processInfo = str;
        eventConsoleTextArea.setText(processInfo + "\n");
    }

    public void setEventConsole(String str) {
        event = str;
        processLookUpTextArea.setText(event + "\n");
    }

    public void commandAquired(){
        cmdInput = "Pass()";
    }

    public void update(int num1, int num2, int num3, int num4, String mainText, String processesInCPU) {
        setProcessResOnePercentage(num1);
        setProcessResTwoPercentage(num2);
        setRamPercentage(num3);
        setVmPercentage(num4);
        setOsSnapshot(mainText);
        setActiveProcesses(processesInCPU);
    }

    public void updateProcessLookUpResult(String info){
        setProcessLookUp(info);
    }

    public void event(String event){
        setProcessLookUp(event);
    }
}
