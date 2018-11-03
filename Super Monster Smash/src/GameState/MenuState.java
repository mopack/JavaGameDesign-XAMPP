package GameState;

import TileMap.Background;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
		"開始",
		"遊戲說明",
		"離開"
	};
	private Color titleColor;
	private Font titleFont;
	private Font font;
	private boolean ShowHintsIsOn = false;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;

		bg = new Background("/Backgrounds/menubg.gif", 1);
		bg.setVector(-0.1, 0);
		
		titleColor = new Color(128, 0, 0);
		titleFont = new Font("mingliub",Font.PLAIN,56);
		font = new Font("mingliub", Font.PLAIN, 24);
		
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Super Monster Smash", 560, 230);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], 780, 350 + i * 30);
		}
		
		if(ShowHintsIsOn){
			g.setColor(Color.BLACK);
			g.drawString("【遊戲操作】", 780, 570);
			g.drawString("  請務必記牢以下按鍵：", 780, 570+28*1);
			g.drawString("  ↑↓←→：移動", 780, 570+28*2);
			g.drawString("  Enter：      確定", 780, 570+28*3);
			g.drawString("  ESC：       取消/離開", 780, 570+28*4);
			g.drawString("  另外，為了避免某些囤積卡片，手牌上限為九。", 780, 570+28*5);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.PICKCHARSTATE);
		}
		if(currentChoice == 1) {
			ShowHintsIsOn = true;
		}
		if(currentChoice == 2) {
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}
	
}










