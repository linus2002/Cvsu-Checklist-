package cvsu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class SplashScreen extends JFrame {

    JProgressBar progressBar;
    private JPanel designScreen;
    BufferedImage campusBackground, cvsuLogo;
    private String logoPath = "C://Users/Admin/programming/midterm/src/images/cvsuLogo.png";
    
    SplashScreen() {
    	
    	try {
    		campusBackground = null;
    		cvsuLogo = null;
    		campusBackground = ImageIO.read(getClass().getResource("/images/bg.png"));
    		cvsuLogo = ImageIO.read(getClass().getResource("/images/cvsuLogo.png"));
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	
        progressBar = new JProgressBar(0, 2000);
        progressBar.setBounds(50, 275, 500, 6);  
        progressBar.setMinimum(0); 
        progressBar.setMaximum(100);
        progressBar.setStringPainted(false);
        progressBar.setBorder(BorderFactory.createLineBorder(Color.decode("#508423")));
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(Color.decode("#508423"));
        add(progressBar);

        designScreen = new DesignScreen();
        designScreen.setBounds(0, 0, 600, 360);
        add(designScreen);

        setSize(600, 360);
        setLayout(null);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(logoPath));
        
        simulateLoading();
    }
    
    private void simulateLoading() {
		  Thread thread = new Thread(() -> {
	            for (int i = 0; i <= 100; i++) {
	                try {
	                    Thread.sleep(70); 
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                progressBar.setValue(i);
	            }
	            if (progressBar.getValue() == 100) {
	            	dispose();
	            	
	            	checklist frame = new checklist();
	            	frame.setVisible(true);
	                
	            }
	        });
	        thread.start();
	   
	}

    class DesignScreen extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D graphics2d = (Graphics2D) graphics.create();
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            graphics2d.drawImage(campusBackground, 0,0,605,360, null);
            graphics2d.drawImage(cvsuLogo, 65,90,150,140, null);
            
            graphics2d.setColor(Color.decode("#FFFFFF"));
            graphics2d.setFont(new Font("ARIAL BLACK", Font.BOLD, 17));
	        graphics2d.drawString("STUDENT CHECKLIST SYSTEM", 223, 155 );          
            graphics2d.setFont(new Font("ENGRAVERS MT", Font.BOLD, 15));
	        graphics2d.drawString("CAVITE STATE UNIVERSITY", 230, 180 );
	        graphics2d.setFont(new Font("ENGRAVERS MT", Font.BOLD, 12));
	        graphics2d.drawString("BACOOR CITY CAMPUS", 293, 198);
	        
	        graphics2d.setStroke(new BasicStroke(3));
	        graphics2d.setColor(Color.decode("#88DC3D")); // Set the color of the line
            graphics2d.drawLine(230, 162, 512, 162);
            
            
            graphics2d.dispose();
        }
    }

    public static void main(String[] args) {
        SplashScreen run = new SplashScreen();
        run.setVisible(true);
    }
}
