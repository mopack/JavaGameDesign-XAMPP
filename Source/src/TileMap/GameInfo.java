package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import GameState.Level1State;

public class GameInfo {
	
	public String Name[];
	public String Skill1Name[];
	public String Skill2Name[];
	public String Skill1Desc[][];
	public String Skill2Desc[][];
	public String SelfDesc[][];
	
	public int MaxHP[];
	public int Index[];
	
	// game data
	public static int MaxCharNumber;
	public static int MySelection;
	
	public GameInfo() {
	}
	
	public String stringChangeLine(String s){
		int k=34;
		int n=s.length();
		int i=0;
		//int re;
		while(i+k<=n){
			s = s.substring(0,i+k-1) + "\r\n" + s.substring(i+k);
			i = i+k+1;
			//re =  JOptionPane.showConfirmDialog(null,  s, "Title", JOptionPane.YES_NO_OPTION);
		}
		return s;
	}
	public void loadMap(String s) {
		//int reply;
		
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in)
			);
			//String delims = "\\s+";
			MaxCharNumber = Integer.parseInt(br.readLine());

			// New
			Name = new String[MaxCharNumber+1];
			Skill1Name = new String[MaxCharNumber+1];
			Skill2Name = new String[MaxCharNumber+1];
			Skill1Desc = new String[MaxCharNumber+1][5];
			Skill2Desc = new String[MaxCharNumber+1][5];
			SelfDesc = new String[MaxCharNumber+1][5];
			MaxHP = new int[MaxCharNumber+1];
			Index = new int[MaxCharNumber+1];
			
			String line;
			int k = 53;
			for(int i = 1; i <=MaxCharNumber; i++) {
				line = br.readLine(); //Index Number
				Name[i] = br.readLine();
				Skill1Name[i] = stringChangeLine(br.readLine());
				
				//Skill1Desc[i]
				for(int j=1;j<=4;j++){ 
					Skill1Desc[i][j] ="";
				}
				line = br.readLine();
				for(int j=1;j<=4;j++){
					if(line.length() >= j*k){
						Skill1Desc[i][j] = line.substring((j-1)*k,j*k);
					}else{
						Skill1Desc[i][j] = line.substring((j-1)*k);
						break;
					}
				}
				
				Skill2Name[i] = br.readLine();
			
				//Skill2Desc[i]
				for(int j=1;j<=4;j++){ 
					Skill2Desc[i][j] ="";
				}
				line = br.readLine();
				for(int j=1;j<=4;j++){
					if(line.length() >= j*k){
						Skill2Desc[i][j] = line.substring((j-1)*k,j*k);
					}else{
						Skill2Desc[i][j] = line.substring((j-1)*k);
						break;
					}
				}
				
				//SelfDesc[i]
				for(int j=1;j<=4;j++){ 
					SelfDesc[i][j] ="";
				}
				line = br.readLine();
				for(int j=1;j<=4;j++){
					if(line.length() >= j*k){
						SelfDesc[i][j] = line.substring((j-1)*k,j*k);
					}else{
						SelfDesc[i][j] = line.substring((j-1)*k);
						break;
					}
				}
				
				MaxHP[i] = Integer.parseInt(br.readLine());
				
				line = br.readLine(); //Space Line
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Select 5 Computer_Players & Random Seats
	public void GetSeats(int currentChoice){
		
		MySelection = currentChoice;
		for(int i=1;i<=6;i++){
			Level1State.CharID[i] = (int)(Math.random()*GameInfo.MaxCharNumber) + 1;
			
			for(int j=1;j<=i-1;j++){
				while(Level1State.CharID[i] == Level1State.CharID[j] || Level1State.CharID[i] == GameInfo.MySelection){
					Level1State.CharID[i] = (int)(Math.random()*GameInfo.MaxCharNumber) + 1;
				}
			}
		}
		Level1State.MyIndex = (int)(Math.random()*6) + 1;
		Level1State.CharID[Level1State.MyIndex] = MySelection;		
		
		 // Set 3 Cite Groups
		int CiteABCAid[] = new int[7];
		
		for(int i=1;i<=6;i++){Level1State.CiteABC[i]=0;}
		for(int i=1;i<=6;i++){CiteABCAid[i]=i;}
		
		for(int k=1;k<=3;k++){
			for(int t=1;t<=2;t++){
				int i = 2*k; 
				if(t == 1){i--;}
				i = 6 - i;
				
				int ret = (int)(Math.random()*i) + 1 , j;
				int fi=0;
				for(j=1;j<=ret;j++){
					fi++;
					while(CiteABCAid[fi]==0){
						fi++;
					}
				}
				CiteABCAid[fi] = 0;
				Level1State.CiteABC[fi]= 64+k;
				//JOptionPane.showConfirmDialog(null, "1~" + Integer.toString(i) + ": ret=" + Integer.toString(ret) + " fi=" + Integer.toString(fi), "Title", JOptionPane.YES_NO_OPTION);
			}

		}
		
		// Set CurrentHP by MaxHP
		for(int i=1;i<=6;i++){
			Level1State.CurrentHP[i] = MaxHP[Level1State.CharID[i]];
		}
		
		Level1State.MsgInitAid[0] = "開始囉! 你是第" + Character.toString((char) Level1State.CiteABC[Level1State.MyIndex]) + "組!!"; //para
		Level1State.MsgInitAid[1] = "好的"; //para
		Level1State.Msg[GameMsg.Msg_RoundStart] = new GameMsg(1,Level1State.MsgInitAid);
	}
	


	
}



















