import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI2 {
    public static void main(String[] args) {
        Abc obj=new Abc();

    }
}
class Abc extends JFrame implements ActionListener {
    JTextField f;
    JTextField f1;
    JButton b;
    JLabel l;
    public Abc() throws HeadlessException {
        setLayout(new FlowLayout());
        f=new JTextField(20);
        f1=new JTextField(20);
        l=new JLabel("Result"); //used to print txt
        JButton b=new JButton("Ok");
        add(f);
        add(f1);
        add(l);
        add(b);
        setVisible(true);
        b.addActionListener(this);
        setSize(800,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int num1=Integer.parseInt(f.getText());
        int num2=Integer.parseInt(f.getText());
        int value = num1+num2;
        l.setText(value+"");
        
    }
}