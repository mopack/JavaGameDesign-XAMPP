package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import Main.GamePanel;
import TileMap.Background;
import TileMap.GameInfo;
import TileMap.TileMap;

public class PickCharState extends GameState {

	private GameInfo Gifo; 
	
	private Background bg;
	
	private int currentChoice;
	private int currentChoice2;
	private int CharMaxNumber = 20;
	private int Steps;

	private BufferedImage ShowCard;
	private String s;
	
	public PickCharState(GameStateManager gsm) {
		
		this.gsm = gsm;

		bg = new Background("/Backgrounds/menubg.gif", 1);
		bg.setVector(-0.1, 0);

		init();
		
	}
	
	public void init() {
		Steps = 0;
		currentChoice = 1;
		currentChoice2 = 1;
		
		Gifo = new GameInfo();
		Gifo.loadMap("/Maps/CharData.txt");
		
	}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		// clear screen
		g.setColor(Color.cyan);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// draw bg
		bg.draw(g);
			
		// draw title
		g.setColor(new Color(128, 0, 0));				//半紅
		g.setFont(new Font("mingliub",Font.PLAIN,28));  //細明體28
		g.drawString("選擇你的角色紙卡", 10, 30);
		
		//上面的N張卡
		for(int i=1;i<=CharMaxNumber;i++){
			
			s = Integer.toString(i);
			try {
				ShowCard =  ImageIO.read( getClass().getResourceAsStream("/Char/90X135/" + s + ".jpg") );
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			g.drawImage(
					ShowCard,
					(int)-85 + i*97 -970*((int)(i/11)),
					(int)50 + 150*((int)(i/11)),
					null
			);
		}
		
		//開始
		try {
			ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/GreenButton 240X81.png") );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g.drawImage(ShowCard,1140,420,null);
		g.drawImage(ShowCard,1140,510,null);
		g.drawImage(ShowCard,1140,600,null);

		g.drawString("開始", 1140+90, 420+50);
		g.drawString("重選", 1140+90, 510+50);
		g.drawString("離開", 1140+90, 600+50);
		
		
	
		//外框
		if (Steps == 0){
			try {
				ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/Pick 99X153.png") );
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			g.drawImage(
					ShowCard,
					(int)-88 + currentChoice*97 -970*((int)(currentChoice/11)),
					(int)40 + 150*((int)(currentChoice/11)),
					null
			);
		}else{
			
			try {
				ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/Pick 260X100.png") );
			} catch (IOException e) {
				e.printStackTrace();
			}		
			
			g.drawImage(
					ShowCard,
					(int)1140 -15,
					(int)320 + currentChoice2*90,
					null
			);
		}

		//顯示大圖的背景
		try {
			ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/SBS 1050X540.png") );
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(ShowCard,30,380,null);
		
		//大圖
		s = Integer.toString(currentChoice);
		try {
			ShowCard =  ImageIO.read( getClass().getResourceAsStream("/Char/255X383/"+ s +".jpg") );
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(ShowCard,30+45,380+115,null);
		
		//Char Data
		g.drawString(Gifo.Name[currentChoice], 30+40, 380+85);
		
		g.setFont(new Font(Font.SERIF, Font.PLAIN,12));
		g.setColor(new Color(0, 0, 0));
		for(int i=1;i<=4;i++){
			g.drawString(Gifo.SelfDesc[currentChoice][i], 30+400, 380+460+12*(i-1));
		}
		
		g.setColor(new Color(255, 255, 255));
		for(int i=1;i<=4;i++){
			g.drawString(Gifo.Skill1Desc[currentChoice][i], 30+367, 380+80+12*(i-1));
		}
		for(int i=1;i<=4;i++){
			g.drawString(Gifo.Skill2Desc[currentChoice][i], 30+367, 380+300+12*(i-1));
		}
		
		g.setFont(new Font(Font.SERIF, Font.PLAIN,36)); //Serif36
		g.setColor(new Color(128, 0, 0));				//半紅
		g.drawString(Integer.toString(Gifo.MaxHP[currentChoice]), 30+305, 380+50);
		
		g.setFont(new Font(Font.SERIF, Font.PLAIN,14));
		g.setColor(new Color(0, 255, 255));
		g.drawString(Gifo.Skill1Name[currentChoice], 30+367,  380+80-20);
		g.drawString(Gifo.Skill2Name[currentChoice], 30+367,  380+300-20);
	}

	
	
	private void select() {
		if(Steps == 0){
			Steps = 1;
			currentChoice2 = 1;
		}else{
			if(currentChoice2 == 1){
				//important
				Gifo.GetSeats(currentChoice);
				
				Steps = 0;
				currentChoice = 1;
				gsm.setState(GameStateManager.LEVEL1STATE);
			}else if(currentChoice2 == 2){
				Steps = 0;
				currentChoice = 1;
			}else{
				Steps = 0;
				currentChoice = 1;
				gsm.setState(GameStateManager.MENUSTATE);
			}
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		
		if(Steps == 0){
			if(k == KeyEvent.VK_UP) {
				if(currentChoice -10 >= 1 ){
					currentChoice -= 10;
				}
			}
			if(k == KeyEvent.VK_DOWN) {
				if(currentChoice +10 <= CharMaxNumber ){
					currentChoice += 10;
				}
			}
			if(k == KeyEvent.VK_LEFT) {
				if(currentChoice != 1) {
					currentChoice--;
				}
			}
			if(k == KeyEvent.VK_RIGHT) {
				if(currentChoice != CharMaxNumber) {
					currentChoice++;
				}
			}
		}else{
			if(k == KeyEvent.VK_UP) {
				if(currentChoice2 != 1 ){
					currentChoice2 --;
				}
			}
			if(k == KeyEvent.VK_DOWN) {
				if(currentChoice2 != 3 ){
					currentChoice2 ++;
				}
			}			
		}
	}
	public void keyReleased(int k) {}
	
}
