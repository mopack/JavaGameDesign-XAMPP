package GameState;

import Main.GamePanel;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.Math;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane; //JOptionPane.showConfirmDialog(null, Integer.toString(123), "Title", JOptionPane.YES_NO_OPTION);


public class Level1State extends GameState {
	// GSM, GInfo, BG
	private BufferedImage ShowCard;
	private BufferedImage GreenButtom;
	private BufferedImage Border;
	private GameInfo Gifo;
	private Background bg;
	
	
	// Temporary Buffer
	String s; //  String
	public static String MsgInitAid[];
	int MsgOptionsNumber;
	
	// Constant Color & Font
	//private Color WordColor = new Color(128, 0, 0);
	//private Font WordFont = new Font("mingliub",Font.PLAIN,28);
	
	// Message Parameters
	public static GameMsg Msg[];
	private int MsgNumber = 50;
	
	boolean ShowOnYouCantKill = false;
	String CantUsedThisSpellStringStatement = "";
	int SeatLocation[][];
	int currentRound = 0;
	int currentMsgType = GameMsg.Msg_RoundStart;
	int currentChoiceUD = 1;
	int currentChoiceLR = 1;
	int GoOnTemp;
	boolean GotoComputerShowTemp;
	int TableToken;
	int WhoAsAim_parameter;
	int WhoAsAim_TTVar;
	int MsgTypeTempforExit;
	boolean goEventCard = false, goSummonKiller = false;
	int ComputerPickHandCard;
	boolean Skill1Used = false , Skill2Used = false;
	int Skill1or2 = 1;
	
	// Controlling Event Cards
	int SacrificedCards[];
	int SacrificedTotalValue;
	int SacrificedCardNumber = 1;
	// Game Data
	public static int CharID[];
	public static int MyIndex;
	public static int CiteABC[];
	public static int CurrentHP[];
	int CardSet[][];
	int CardSetVar[][];
	int CardValue[] = {0, 1, 2, 5, 4, 2, 2, 2, 3, 2, 3};
	String EventCardName[] = {"","�ձ�","���","�L��","�Ѩa","���f","�洫","����","�^��","�D�U","�s��"};
	// Other Game Data
	
	// Abnormal Status
	boolean isTerrified[];				//3����P�l - ������߷W�W - ���ߪ��A
	boolean isRestrained[];				//4��Q - ��c���E�� - �E�ꪬ�A
	boolean cantAttackAllie[];			//5��� - �����ǽ�  - �L�k������� 
	boolean isSqueezed[];				//9�J�W - �F���y�b - �^�����A
	
	
	// Round-Start Status
	boolean RabbitsAlertIsOn = false;	//6�Ⱖ�ߤl - ���궧���U���ȫ�^�C - ���M�D�]
	boolean RabbitsIsHit = false;		
	int SixChildrenAvailable = 0;		//10�s�]���l - ���l���L
	boolean SixChildrenIsOn = false;	
	boolean RedHellIsOn = false;		//19�\���� - �����a�� - �����a���o��
	
	// Character Skill's Data
	
	
	

	boolean RamdonRate(double percentage){ //25% -> percentage=25
		double ret = Math.random()*100;
		if(ret >= percentage){return true;}
		else{return false;}
	}
	
	void Hurt(int who, int HurtPoint){
		// Attacked Skill
		
		// If no Skill works
		CurrentHP[who]-=HurtPoint;
		
		// End Process
		int one;
		boolean isEnd = true;
		for(one=1;one<=6;one++){
			if(CurrentHP[one] >= 1 ){
				break;
			}
		}
		
		for(int i=one+1;i<=6;i++){
			if(CurrentHP[i] >= 1 && CiteABC[one]!=CiteABC[i]){
				isEnd = false;
				break;
			}
		}
		if(isEnd){
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	void discardAnEventCard(int who,int index){
		for(int i=index+1;i<=CardSet[who][0];i++){
			CardSet[who][i-1] = CardSet[who][i];
			CardSetVar[who][i-1] = CardSetVar[who][i];
		}
		CardSet[who][0]--;
	}
	void getAnEventCard(int ForWhatIndex){
		if(CardSet[ForWhatIndex][0]>=9){return;}
		++CardSet[ForWhatIndex][0];
		
		int pick = (int)(Math.random()*65) + 1,ret=1;
		if(pick <= 15){
			ret = 1;
			CardSetVar[ForWhatIndex][ CardSet[ForWhatIndex][0] ] = (int)(Math.random()*6) + 1;
		}else if(pick <= 22){
			ret = 2;
		}else if(pick <= 24){
			ret = 3;
		}else if(pick <= 27){
			ret = 4;
		}else if(pick <= 34){
			ret = 5;
		}else if(pick <= 41){
			ret = 6;
		}else if(pick <= 48){
			ret = 7;
			CardSetVar[ForWhatIndex][ CardSet[ForWhatIndex][0] ] = 81* ( (int)(Math.random()*8) + 1);
			CardSetVar[ForWhatIndex][ CardSet[ForWhatIndex][0] ] += 9* ( (int)(Math.random()*8) + 1);
			CardSetVar[ForWhatIndex][ CardSet[ForWhatIndex][0] ] +=    ( (int)(Math.random()*8) + 1);
		}else if(pick <= 53){
			ret = 8;
		}else if(pick <= 60){
			ret = 9;
			CardSetVar[ForWhatIndex][ CardSet[ForWhatIndex][0] ] = (int)(Math.random()*3) + 3;
		}else{
			ret = 10;
		}
		CardSet[ForWhatIndex][ CardSet[ForWhatIndex][0] ] = ret;
	}
	
	private void MsgShow(int MsgTypeIndex, Graphics2D g){
		
		if(MsgTypeIndex==0){return;}
		
		if(currentMsgType != GameMsg.Msg_ReadImformation){
			int leftTopX=0, leftTopY=0 ;
			
			g.setColor(new Color(102,170,255));
			g.fillRect(leftTopX, leftTopY, 1050, 540); 
			
			g.setColor(new Color(128,0,0));
			g.setFont(new Font("mingliub",Font.PLAIN,28));
			g.drawString(Msg[MsgTypeIndex].getStatement(), leftTopX+40, leftTopY+50);

			for(int i=1;i<=Msg[MsgTypeIndex].getOptionsNumber();i++){
				g.drawImage(GreenButtom,leftTopX+30,leftTopY+90*i,null);
				g.drawString(Msg[MsgTypeIndex].getOptions(i), leftTopX+30+40, leftTopY+90*i+50);
				if(currentChoiceUD == i){
					g.drawImage(Border,leftTopX+30-30, leftTopY+90*i-10, null);
				}
			}
		}else{
	
		}
		
	}
	
	public Level1State(GameStateManager gsm) {
	
		// GSM, GInfo, BG
		this.gsm = gsm;
		
		Gifo = new GameInfo();
		Gifo.loadMap("/Maps/CharData.txt");
		
		bg = new Background("/Backgrounds/menubg.gif", 1);
		bg.setVector(-0.1, 0);
		
		try {
			GreenButtom =  ImageIO.read( getClass().getResourceAsStream("/HUD/GreenButton 480X81.png") );
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Border =  ImageIO.read( getClass().getResourceAsStream("/HUD/Pick 520X100.png") );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Basic Index Data New Declare 
		CharID = new int[7];
		CurrentHP = new int[7];
		CardSet = new int[7][10];
		CardSetVar = new int[7][10];
		CiteABC = new int[7];
		SeatLocation = new int[7][2];
		SacrificedCards = new int[10];
		
		// Set Initial CardSet 
		for(int i=1;i<=6;i++){CardSet[i][0]=0;}
		for(int i=1;i<=6;i++){
			for(int j=1;j<=5;j++){
				getAnEventCard(i);
			}
		}
		
		// Seat Location
		SeatLocation[1][0] = 1425; SeatLocation[1][1] = 25;
		SeatLocation[2][0] = 1655; SeatLocation[2][1] = 190;
		SeatLocation[3][0] = 1655; SeatLocation[3][1] = 410;
		SeatLocation[4][0] = 1425; SeatLocation[4][1] = 520;
		SeatLocation[5][0] = 1125; SeatLocation[5][1] = 440;
		SeatLocation[6][0] = 1100; SeatLocation[6][1] = 100;
				
		// Game MsgBox Data Setting
		Msg = new GameMsg[MsgNumber]; //MsgNumber
		
		currentMsgType = GameMsg.Msg_RoundStart; //para
		MsgOptionsNumber = 1; //para
		MsgInitAid= new String[MsgOptionsNumber+1];

		
		init();

	}
	
	public void init() {
	}
	
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// GSM, GInfo, BG -----------------------------------------------------------------------------//
		// clear screen
		//g.setColor(Color.WHITE);
		//g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// draw bg
		bg.draw(g);
		
		// LeftUp --------------------------------------------------------------------------------------//
		// Draw LeftUp Message Box
		MsgShow(currentMsgType,g);

		// Draw LeftUp BigPicture
		if(currentMsgType == GameMsg.Msg_ReadImformation){
			
			int BigLeftX =0, BigLeftY = 0;
			//BigPicture's Background
			try {
				ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/SBS 1050X540.png") );
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(ShowCard,BigLeftX,BigLeftY,null);
			
			
			//BigPicture
			s = Integer.toString(CharID[currentChoiceLR]);
			try {
				ShowCard =  ImageIO.read( getClass().getResourceAsStream("/Char/255X383/"+ s +".jpg") );
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(ShowCard,BigLeftX+45,BigLeftY+115,null);
		
			//Char Data
			g.setColor(new Color(128, 0, 0));				//�b��
			g.setFont(new Font("mingliub",Font.PLAIN,28));  //�ө���28
			g.drawString(Gifo.Name[CharID[currentChoiceLR]], BigLeftX+40, BigLeftY+85);
		
			g.setFont(new Font(Font.SERIF, Font.PLAIN,12));
			g.setColor(new Color(0, 0, 0));
			for(int i=1;i<=4;i++){
				g.drawString(Gifo.SelfDesc[CharID[currentChoiceLR]][i], BigLeftX+400, BigLeftY+460+12*(i-1));
			}
			
			g.setColor(new Color(255, 255, 255));
			for(int i=1;i<=4;i++){
				g.drawString(Gifo.Skill1Desc[CharID[currentChoiceLR]][i], BigLeftX+367, BigLeftY+80+12*(i-1));
			}
			for(int i=1;i<=4;i++){
				g.drawString(Gifo.Skill2Desc[CharID[currentChoiceLR]][i], BigLeftX+367, BigLeftY+300+12*(i-1));
			}
			
			g.setFont(new Font(Font.SERIF, Font.PLAIN,36));
			g.setColor(new Color(128, 0, 0));
			g.drawString(Integer.toString(Gifo.MaxHP[CharID[currentChoiceLR]]), BigLeftX+305, BigLeftY+50);
			
			g.setFont(new Font(Font.SERIF, Font.PLAIN,14));
			g.setColor(new Color(0, 255, 255));
			g.drawString(Gifo.Skill1Name[CharID[currentChoiceLR]], BigLeftX+367,  BigLeftY+80-20);
			g.drawString(Gifo.Skill2Name[CharID[currentChoiceLR]], BigLeftX+367,  BigLeftY+300-20);
		}
		
		
		// Draw Round Table --------------------------------------------------------------------------------//
		try {
			ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/Table 402X432.png") );
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(ShowCard,1050+200,60,null);

		// Draw Round Table Plays Name 
		g.setFont(new Font("mingliub",Font.PLAIN,28));
		
		for(int i=1;i<=6;i++){
			if(i == MyIndex){g.setColor(new Color(0, 0, 128)); s = "��";} else {g.setColor(new Color(128, 0, 0)); s="�q��";}
			s = Gifo.Name[CharID[i]] +" "+s; //+ Character.toString((char)CiteABC[i]) +"��";
			if(CurrentHP[i] <= 0){s+="(��)";}
			g.drawString(s, SeatLocation[i][0] , SeatLocation[i][1]);
			
			s = "HP="+CurrentHP[i]+" ��P��="+ Integer.toString(CardSet[i][0]);
			g.drawString(s, SeatLocation[i][0] , SeatLocation[i][1] + 26);
		}
		
		// Draw Round Table Pick Border
		if(currentMsgType == GameMsg.Msg_ReadImformation || currentMsgType == GameMsg.Msg_WhoAsAim || currentMsgType == GameMsg.Msg_SelectTarget){
			try {
				ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/Pick 260X50.png") );
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(ShowCard,SeatLocation[currentChoiceLR][0]-20,SeatLocation[currentChoiceLR][1]-35,null);
			
		}
		
		// Draw Round Table Abnormal Status
		
		// Draw Event Cards --------------------------------------------------------------------------------//
		for(int i=1;i<=CardSet[MyIndex][0];i++){
			
			// Show Pictures
			try {
				ShowCard =  ImageIO.read( getClass().getResourceAsStream("/Event/"+Integer.toString(CardSet[MyIndex][i])+".png") );
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(ShowCard,10+(i-1)*210,560,null);
			
			// Show Statements
			g.setColor(new Color(0,0,0));
			g.setFont(new Font("mingliub",Font.PLAIN,12));
			int Xi=10+(i-1)*210+22, Yi =560+210;
			switch(CardSet[MyIndex][i]){
				case 1:
					if(CardSetVar[MyIndex][i] <=3){
						g.drawString("���@�ؼСA�Y"+Character.toString((char)(CardSetVar[MyIndex][i]+64))+"�աA���@" ,Xi ,Yi);
						g.drawString("�i�d" ,Xi ,Yi+14);
					}else{
						g.drawString("���@�ؼСA�Y"+Character.toString((char)(CardSetVar[MyIndex][i]-3+64))+"�աA��@�i" ,Xi ,Yi);
						g.drawString("�d" ,Xi ,Yi+14);
					}
					break;
				case 2:
					g.drawString("�i�N����f�P�̧�������" ,Xi ,Yi);
					g.drawString("�`�ಾ����L����" ,Xi ,Yi+14);
					break;
				case 3:
					g.drawString("�i�N���@�o�V�ۤv���\��" ,Xi ,Yi);
					g.drawString("�d�L��" ,Xi ,Yi+14);
					break;
				case 4:
					g.drawString("��L�����2�I�ˮ`" ,Xi ,Yi);
					break;
				case 5:
					g.drawString("�A���i�d" ,Xi ,Yi); 
					break;
				case 6:
					g.drawString("�P���w���⴫�d" ,Xi ,Yi); 
					break;
				case 7:
					g.drawString("���榹�T���O�G" ,Xi ,Yi);
					int temp[] = new int[4];
					temp[1] = (int) (CardSetVar[MyIndex][i]/81);
					temp[2] = (int) ((CardSetVar[MyIndex][i] - 81 * temp[1])/9);
					temp[3] = (int) CardSetVar[MyIndex][i] - temp[1]*81 - temp[2]*9;
					for(int j=1;j<=3;j++){
						switch(temp[j]){
							case 1: s = "�ۤv��1�i�P"; break;
							case 2: s = "�ۤv���h1�i�P"; break;
							case 3: s = "�k�䪺�H��1�i�P"; break;
							case 4: s = "���䪺�H��1�i�P"; break;
							case 5: s = "�ﭱ���H��1�i�P"; break;
							case 6: s = "A�ե�1�H�o1�ƥ�P"; break;
							case 7: s = "B�ե�1�H�o1�ƥ�P"; break;
							case 8: s = "C�ե�1�H�o1�ƥ�P"; break;
						}
						g.drawString(s ,Xi ,Yi+14*j);
					}
					break;
				case 8:
					g.drawString("�۸ɦ�2�I" ,Xi ,Yi);
					break;
				case 9:
					g.drawString("��C��s�ɡA���d�N�߰�" ,Xi ,Yi);
					g.drawString("��l���a�A�H"+CardSetVar[MyIndex][i]+"�I�d�Ȫ���" ,Xi ,Yi+14);
					g.drawString("���A�ϧA����" ,Xi ,Yi+14*2);
					break;
				case 10:
					g.drawString("�o����w���⪺�@��P" ,Xi ,Yi);
					break;
				default:
					break;
			}
			
			// Draw Event Card Border 
			if(currentMsgType == GameMsg.Msg_SummonKiller || currentMsgType == GameMsg.Msg_SelectEventCards){
				if(SacrificedCards[i] == 1){
					try {
						ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/Selected.png") );
					} catch (IOException e) {
						e.printStackTrace();
					}
					g.drawImage(ShowCard,Xi,Yi,null);
				}
			}
			
			// Show ValuePoints
			int tempOutput;
			switch(CardSet[MyIndex][i]){
				case 1: 
					tempOutput = 1; break;
				case 3: 
					tempOutput = 5; break;
				case 4:
					tempOutput = 4; break;
				case 8:
				case 10:
					tempOutput = 3; break;
				default:
					tempOutput = 2; break;
			}
			 Xi=10+(i-1)*210+95; Yi =560+210-35;
			 g.drawString(Integer.toString(tempOutput) ,Xi ,Yi);
			 

		}
		
		// Select Card 
		if(currentMsgType == GameMsg.Msg_SummonKiller || currentMsgType == GameMsg.Msg_PickAnEventCard || currentMsgType == GameMsg.Msg_SelectEventCards){
			int Xi=10+(currentChoiceLR-1)*210, Yi =560;
			try {
				ShowCard =  ImageIO.read( getClass().getResourceAsStream("/HUD/Pick 240X365.png") );
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(ShowCard,Xi-20,Yi-33,null);
		}
		
		// Information Shows
		if(currentMsgType == GameMsg.Msg_SummonKiller || currentMsgType == GameMsg.Msg_SelectEventCards){ 
			g.setColor(new Color(128,0,0));
			g.setFont(new Font("mingliub",Font.PLAIN,22));
			
			if(currentMsgType == GameMsg.Msg_SummonKiller ){
				g.drawString("�ثe�묹�`�ȡG" + Integer.toString(SacrificedTotalValue), 10, 500);
			}else if(currentMsgType == GameMsg.Msg_SelectEventCards){
				g.drawString("�ثe�묹�d�ơG" + Integer.toString(SacrificedCards[0]), 10, 500);
			}
			
			//Can't Kill Message
			if(ShowOnYouCantKill){
				g.setColor(new Color(0,255,255));
				g.drawString("�����H�l��!!!!!!�묹�ȶ��j�󵥩�|!!!!", 10, 475);
			}
			
		}
		
		// ESC Message
		if(currentMsgType == GameMsg.Msg_ReadImformation){
			g.setColor(new Color(0,0,0));
			g.setFont(new Font("mingliub",Font.PLAIN,28));
			g.drawString("��ESC���}", 1060, 520);
		}
		
		//Can't Kill Message		
		if(currentMsgType == GameMsg.Msg_WhoAsAim || currentMsgType == GameMsg.Msg_SelectTarget){
			if(ShowOnYouCantKill){
				g.setColor(new Color(0,0,0));
				g.setFont(new Font("mingliub",Font.PLAIN,28));
				g.drawString("�����ۤv!!", 10, 475);
			}
		}
		
		// Can't Use Spell Message
		if(CantUsedThisSpellStringStatement != ""){
			g.setColor(new Color(0,0,0));
			g.drawString(CantUsedThisSpellStringStatement, 10, 500);
		}
		
		// Round Number & Your Group
		g.setColor(new Color(0,0,0));
		g.setFont(new Font("mingliub",Font.PLAIN,28));
		g.drawString("��"+Integer.toString(currentRound)+"�^�X,�A�O��" + Character.toString((char) CiteABC[MyIndex]) + "��!!", 10, 905);
	}
	

	
	private void select() {
		if(currentMsgType == GameMsg.Msg_RoundStart){
			currentRound++;
			for(int i=1;i<=6;i++){
				getAnEventCard(i);
			}
			
			//Next State Create
			currentMsgType = GameMsg.Msg_RoundStartSkillExcludeYou;
			MsgOptionsNumber = 1;
			MsgInitAid= new String[MsgOptionsNumber+1];
			MsgInitAid[0] = "��" + currentRound + "���}�l!! �o�P����!!";
			MsgInitAid[1] = "�n��";
			Msg[currentMsgType] = new GameMsg(MsgOptionsNumber, MsgInitAid);
			
			// Initialize Temps for Next State
			GoOnTemp = 1;
			GotoComputerShowTemp = false;
			
		}else if(currentMsgType == GameMsg.Msg_RoundStartSkillExcludeYou){
			for(int i=GoOnTemp;i<=6;i++){
				if(i!=MyIndex){
					
					if(CharID[i]==6){
						RabbitsAlertIsOn = true; 
						RabbitsIsHit = false;
						
						//Next State Create
						currentMsgType = GameMsg.Msg_RoundStartSkillComputerShow;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = Gifo.Name[6] + "�ϥΡG" + Gifo.Skill2Name[6] + "!!!";
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						
						// Used Temps to Record as Coming Back
						GoOnTemp = i;
						GotoComputerShowTemp = true;
						break;
					}else if(CharID[i]==10){
						SixChildrenIsOn = true; 
						SixChildrenAvailable = 6;
						
						//Next State Create
						currentMsgType = GameMsg.Msg_RoundStartSkillComputerShow;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = Gifo.Name[10] + "�ϥ� " + Gifo.Skill2Name[10] + "!!!";
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber, MsgInitAid);
						
						// Used Temps to Record as Coming Back
						GoOnTemp = i;
						GotoComputerShowTemp = true;					
						break;					
						
					}else if(CharID[i]==19 && CurrentHP[i] >=6){
						CurrentHP[i]-=5;
						RedHellIsOn= true;
	
						//Next State Create
						currentMsgType = GameMsg.Msg_RoundStartSkillComputerShow;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = Gifo.Name[19] + "�ϥ� " + Gifo.Skill1Name[19] + "!!!";
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber, MsgInitAid);

						// Used Temps to Record as Coming Back
						GoOnTemp = i;
						GotoComputerShowTemp = true;
						break;			
					}
				}
			}
			
			if(GotoComputerShowTemp == false){			
				if(CharID[MyIndex]==6 || CharID[MyIndex]==10){
					//Next State Create
					currentMsgType = GameMsg.Msg_YourRoundStartSkillRequest;
					MsgOptionsNumber = 2;
					MsgInitAid= new String[MsgOptionsNumber+1];
					MsgInitAid[0] = "�A���G" + Gifo.Skill2Name[CharID[MyIndex]] + "�C�n�ζ�?"; 
					MsgInitAid[1] = "�n��"; 
					MsgInitAid[2] = "���n";
					Msg[currentMsgType] = new GameMsg(2,MsgInitAid);
					
				}else if(CharID[MyIndex]==19 && CurrentHP[MyIndex] >=6){
					//Next State Create
					currentMsgType = GameMsg.Msg_YourRoundStartSkillRequest;
					MsgOptionsNumber = 2;
					MsgInitAid= new String[MsgOptionsNumber+1];
					MsgInitAid[0] = "�A���G" + Gifo.Skill1Name[19] + "�C�n�ζ�?";
					MsgInitAid[1] = "�n��"; 
					MsgInitAid[2] = "���n";
					Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
	
				}else{
					TableToken = 1;
					//Next State Create
					currentMsgType = GameMsg.Msg_RoundingARound;
					MsgOptionsNumber = 1;
					MsgInitAid= new String[MsgOptionsNumber+1];
					MsgInitAid[0] = "����" + Gifo.Name[CharID[TableToken]] + "!";
					MsgInitAid[1] = "�n��";
					Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
				}
			}
			
		}else if(currentMsgType == GameMsg.Msg_RoundStartSkillComputerShow){
			GoOnTemp++;
			GotoComputerShowTemp = false;
			
			currentMsgType = GameMsg.Msg_RoundStartSkillExcludeYou;
			MsgOptionsNumber = 1;
			MsgInitAid= new String[MsgOptionsNumber+1];
			
			MsgInitAid[0] = Gifo.Name[CharID[GoOnTemp -1]] + "��ܡG�A�̧��J��!";
			MsgInitAid[1] = "�n��";
			Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
			
		}else if(currentMsgType == GameMsg.Msg_YourRoundStartSkillRequest){
			if(currentChoiceUD == 1){
				if(CharID[MyIndex]==6){
					RabbitsAlertIsOn = true; 
					RabbitsIsHit = false;
				}else if(CharID[MyIndex]==10){
					SixChildrenIsOn = true; 
					SixChildrenAvailable = 6;
				}else if(CharID[MyIndex]==19){
					CurrentHP[MyIndex]-=5;
					RedHellIsOn= true;
				}
			}
			
			//
			TableToken = 1;
			//Next State Create
			currentMsgType = GameMsg.Msg_RoundingARound;
			MsgOptionsNumber = 1;
			MsgInitAid= new String[MsgOptionsNumber+1];
			MsgInitAid[0] = "����" + Gifo.Name[CharID[TableToken]] + "!";
			MsgInitAid[1] = "�n��";
			Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
		
		}else if(currentMsgType == GameMsg.Msg_RoundingARound){
			if(TableToken == MyIndex){
				
				//Next State Create
				currentMsgType = GameMsg.Msg_MyTurn;
				MsgOptionsNumber = 5;
				MsgInitAid= new String[MsgOptionsNumber+1];
				MsgInitAid[0] = "�ڲ{�b�Ӱ�����H";
				MsgInitAid[1] = "�ݤj�a���ȥd����";
				MsgInitAid[2] = "�ϥΧޯ�";
				MsgInitAid[3] = "�ϥΨƥ�d";
				MsgInitAid[4] = "�l��f�P��";
				MsgInitAid[5] = "�����ڪ��^�X";
				Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
			}else{
				//Next State Create
				currentMsgType = GameMsg.Msg_ComputersTurn;
				MsgOptionsNumber = 1;
				MsgInitAid= new String[MsgOptionsNumber+1];
				MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "�G�ڻ����A��!";
				MsgInitAid[1] = "�n��";
				Msg[GameMsg.Msg_ComputersTurn] = new GameMsg(MsgOptionsNumber,MsgInitAid);
			}

		}else if(currentMsgType == GameMsg.Msg_MyTurn){
			switch(currentChoiceUD){
				case 1:
					currentChoiceLR = MyIndex;
					currentMsgType = GameMsg.Msg_ReadImformation;
					break;
				case 2:
					currentMsgType = GameMsg.Msg_ActiveSpillShow;
					MsgOptionsNumber = 3;
					MsgInitAid= new String[MsgOptionsNumber+1];
					MsgInitAid[0] = "��@�ޯ�G";
					MsgInitAid[1] = Gifo.Skill1Name[CharID[TableToken]];
					MsgInitAid[2] = Gifo.Skill2Name[CharID[TableToken]];
					MsgInitAid[3] = "�ک���";
					Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					break;
					
				case 3:
					//Next State Create
					currentMsgType = GameMsg.Msg_PickAnEventCard;
					MsgOptionsNumber = 2;
					MsgInitAid= new String[MsgOptionsNumber+1];
					MsgInitAid[0] = "��@�i�ƥ�d�G";
					MsgInitAid[1] = "�M�w����";
					MsgInitAid[2] = "�ک���";
					Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					break;
				case 4:
					currentChoiceLR = 1;
					SacrificedTotalValue = 0;
					for(int i=0;i<=9;i++){
						SacrificedCards[i] = 0;
					}
					
					//Next State Create
					currentMsgType = GameMsg.Msg_SummonKiller;
					MsgOptionsNumber = 3;
					MsgInitAid= new String[MsgOptionsNumber+1];
					MsgInitAid[0] = "�п�ܱ�P�A�P�ȶ��j�󵥩�4�G";
					MsgInitAid[1] = "�[�o�i/���[�o�i";
					MsgInitAid[2] = "�M�w����";
					MsgInitAid[3] = "�ڤ��Q��";
					Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					break;
				case 5:
					// End of Player's Motion
					if(TableToken <=5){
						TableToken++;
						Skill1Used = false; Skill2Used = false;
						//Next State Create
						currentMsgType = GameMsg.Msg_RoundingARound;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = "����" + Gifo.Name[CharID[TableToken]] + "!";
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					}else{
						TableToken = 1;
						currentMsgType = GameMsg.Msg_RoundEnd;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = "���^�X����!!";
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					}
					break;
			}
			currentChoiceUD = 1;
			
		}else if(currentMsgType == GameMsg.Msg_ActiveSpillShow){
			if(currentChoiceUD == 3){
				currentChoiceUD = 2;
				currentMsgType = GameMsg.Msg_MyTurn;
			}else{
				switch(CharID[TableToken]){
				case 1:
					if(Skill1Used && currentChoiceUD == 1){
						CantUsedThisSpellStringStatement = "�ޯ�@�N�o��";
					}else if(CardSet[TableToken][0] < 4 && currentChoiceUD == 2){
						CantUsedThisSpellStringStatement = "��P����";
					}else{
						if(currentChoiceUD == 1){
							Skill1Used = true;
							for(int i=1;i<=3;i++){
								getAnEventCard(TableToken);
							}
							//Next State Create
							currentMsgType = GameMsg.Msg_MyTurn;
						}else{
							currentChoiceLR = 1;
							SacrificedTotalValue = 0;
							for(int i=0;i<=9;i++){
								SacrificedCards[i] = 0;
							}		
							
							SacrificedCardNumber =4;
							Skill1or2 = 2;
							currentChoiceUD = 1;
							//Next State Create
							currentMsgType = GameMsg.Msg_SelectEventCards;
							MsgOptionsNumber = 3;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = "�п�ܱ�P�A����4�i�H�W�G";
							MsgInitAid[1] = "�[�o�i/���[�o�i";
							MsgInitAid[2] = "�M�w����";
							MsgInitAid[3] = "�ڤ��Q��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						}
					}
					break;
				case 2:
					if( (Skill1Used && currentChoiceUD == 1) || (Skill2Used && currentChoiceUD == 2) ){
						CantUsedThisSpellStringStatement = "�ޯ�@�N�o��";
					}else if( (CardSet[TableToken][0] <2 && currentChoiceUD == 1) || (CardSet[TableToken][0] <4 && currentChoiceUD == 2) ){
						CantUsedThisSpellStringStatement = "��P����";
					}else{
						currentChoiceLR = 1;
						SacrificedTotalValue = 0;
						for(int i=0;i<=9;i++){
							SacrificedCards[i] = 0;
						}
						
						
						//Next State Create
						currentMsgType = GameMsg.Msg_SelectEventCards;
						MsgOptionsNumber = 3;
						MsgInitAid= new String[MsgOptionsNumber+1];
						
						if(currentChoiceUD == 1){
							Skill1or2 = 1;
							SacrificedCardNumber =2;
							MsgInitAid[0] = "�п�ܱ�P�A����2�i�H�W�G";
						}else{
							Skill1or2 = 2;
							SacrificedCardNumber =4;
							MsgInitAid[0] = "�п�ܱ�P�A����4�i�H�W�G";
						}
						
						MsgInitAid[1] = "�[�o�i/���[�o�i";
						MsgInitAid[2] = "�M�w����";
						MsgInitAid[3] = "�ڤ��Q��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						
						currentChoiceUD = 1;
					}
				}
			}
		}else if(currentMsgType == GameMsg.Msg_SelectEventCards){
			switch(currentChoiceUD){
			case 1:
				if(SacrificedCards[currentChoiceLR] == 0){
					SacrificedCards[currentChoiceLR] = 1;
					SacrificedCards[0]++;
					SacrificedTotalValue += CardValue[CardSet[TableToken][currentChoiceLR]];
				}else{
					SacrificedCards[currentChoiceLR] = 0;
					SacrificedCards[0]--;
					SacrificedTotalValue -= CardValue[CardSet[TableToken][currentChoiceLR]];
				}
				break;
			case 2:
				if(SacrificedCards[0] >= SacrificedCardNumber){
					for(int i=1;i<=9;i++){
						if(SacrificedCards[i] == 1){
							discardAnEventCard(TableToken,i);
						}
					}
					
					// Effect
					switch(CharID[TableToken]){
					case 1:
						CurrentHP[TableToken]+=2; if(CurrentHP[TableToken]> Gifo.MaxHP[CharID[TableToken]]) {CurrentHP[TableToken]= Gifo.MaxHP[CharID[TableToken]];}
						//Next State Create
						currentMsgType = GameMsg.Msg_MyTurn;
						break;
					case 2:
						if(Skill1or2==1){
							Skill1Used = true;
						}else{
							Skill2Used = true;
						}
						
						currentChoiceLR = 1;
						//Next State Create
						currentMsgType = GameMsg.Msg_SelectTarget;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = "���k���w�@��ؼ�";
						MsgInitAid[1] = "�M�w��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						//JOptionPane.showConfirmDialog(null, Integer.toString(123), "Title", JOptionPane.YES_NO_OPTION);
						break;
					}
					
					
				}else{
					ShowOnYouCantKill = true;
				}
				
				break;
			case 3:
				
				currentChoiceUD = 2;
				currentChoiceLR = 1;
				//JOptionPane.showConfirmDialog(null, Integer.toString(456), "Title", JOptionPane.YES_NO_OPTION);
				currentMsgType = GameMsg.Msg_MyTurn;
				break;
			}
		}else if(currentMsgType == GameMsg.Msg_SelectTarget){
			//JOptionPane.showConfirmDialog(null, Integer.toString(993), "Title", JOptionPane.YES_NO_OPTION);
			if(currentChoiceLR == TableToken){
				ShowOnYouCantKill = true;
			}else{
				switch(CharID[TableToken]){
				case 2:
					if(Skill1or2 == 1){
						//JOptionPane.showConfirmDialog(null, Integer.toString(1), "Title", JOptionPane.YES_NO_OPTION);
						Hurt(currentChoiceLR,3);
					}else{
						//JOptionPane.showConfirmDialog(null, Integer.toString(2), "Title", JOptionPane.YES_NO_OPTION);
						int tempHP = CurrentHP[TableToken];
						
						CurrentHP[TableToken] = CurrentHP[currentChoiceLR]; 
						if(CurrentHP[TableToken]>Gifo.MaxHP[CharID[TableToken]]) {CurrentHP[TableToken]=Gifo.MaxHP[CharID[TableToken]];}
						
						CurrentHP[currentChoiceLR] = tempHP; 
						if(CurrentHP[currentChoiceLR]>Gifo.MaxHP[CharID[currentChoiceLR]]) {CurrentHP[currentChoiceLR]=Gifo.MaxHP[CharID[currentChoiceLR]];}
						//JOptionPane.showConfirmDialog(null, Integer.toString(Gifo.MaxHP[CharID[TableToken]])+"+"+Integer.toString(Gifo.MaxHP[CharID[currentChoiceLR]]), "Title", JOptionPane.YES_NO_OPTION);
					}
					break;
				}
			}
			currentMsgType = GameMsg.Msg_MyTurn;
		}else if(currentMsgType == GameMsg.Msg_ComputersTurn){
			// Computer's AI for Skills
			
			// Random Behavior
			int ret = (int) Math.random() * 4 + 1;
			boolean toYou = false;
			
			switch(ret){
				case 1: 
					goEventCard = true; 
					goSummonKiller = false; 
					break;
				case 2: 
					goEventCard = false; 
					goSummonKiller = true; 
					break;
				case 3:
				case 4:
					goEventCard = true;
					goSummonKiller = true;
					break;
			}
			
			if(goEventCard){
				// Event Card Use
				for(int i=1;i<=CardSet[TableToken][0];i++){
					if(CardSet[TableToken][i] != 2 && CardSet[TableToken][i] != 3 && CardSet[TableToken][i] != 9){
						ComputerPickHandCard = i;
						break;
					}
					
				}
				
				switch(CardSet[TableToken][ComputerPickHandCard]){
				case 1:
					// To who?
					toYou =RamdonRate(70);
					if(toYou){
						currentChoiceLR = MyIndex;
					}else{
						currentChoiceLR = (int) Math.random() * 4 + 1;
						if(currentChoiceLR == MyIndex || currentChoiceLR  == TableToken){currentChoiceLR++;}
					}
					
					// Copy
					WhoAsAim_TTVar = CardSetVar[TableToken][currentChoiceLR];
					discardAnEventCard(TableToken, currentChoiceLR);
					
					if(CardSet[currentChoiceLR][0] >= 9){
						//Next State Create
						currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "��" + Gifo.Name[CharID[currentChoiceLR]] +"�ϥθձ��C"  + "�]���ؼЪ���P�w�F�̦h���E�i�A���ƥ�@�o";
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);							
					}else{
						CardSet[currentChoiceLR][++CardSet[currentChoiceLR][0]] = 1;
						CardSetVar[currentChoiceLR][CardSet[currentChoiceLR][0]] = WhoAsAim_TTVar;
						
						//Next State Create
						currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
													
						if(WhoAsAim_TTVar<=3) {
							
							if(CiteABC[currentChoiceLR] == WhoAsAim_TTVar + 64){
								ret = (int) Math.random() * CardSet[currentChoiceLR][0] + 1;
								discardAnEventCard(currentChoiceLR,ret);
								MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "��" + Gifo.Name[CharID[currentChoiceLR]] +"�ϥθձ��C"  + Gifo.Name[CharID[currentChoiceLR]] + "�����d�A��@�i�P!";
							}else{
								MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "��" + Gifo.Name[CharID[currentChoiceLR]] +"�ϥθձ��C"  + Gifo.Name[CharID[currentChoiceLR]] + "�q�q�����d�A�S���R!";
							}
							
						}else{
							WhoAsAim_TTVar-=3;
							if(CiteABC[currentChoiceLR] == WhoAsAim_TTVar + 64){
								getAnEventCard(currentChoiceLR);
								MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "��" + Gifo.Name[CharID[currentChoiceLR]] +"�ϥθձ��C"  + Gifo.Name[CharID[currentChoiceLR]] + "�����d�A��@�i�P!";
							}else{
								MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "��" + Gifo.Name[CharID[currentChoiceLR]] +"�ϥθձ��C"  + Gifo.Name[CharID[currentChoiceLR]] + "�q�q�����d�A�S���R!";
							}
						}
						
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					}
					break;
					
				case 4:
					discardAnEventCard(TableToken,ComputerPickHandCard);
					
					for(int j=1;j<=6;j++){
						if(j!=TableToken){
							Hurt(j,2);
						}
					}
					break;
				case 5:
					discardAnEventCard(TableToken,ComputerPickHandCard);
					getAnEventCard(TableToken);
					getAnEventCard(TableToken);
					break;
				case 6:
					discardAnEventCard(TableToken,ComputerPickHandCard);
					
					// To who?
					toYou =RamdonRate(70);
					if(toYou){
						// To You
						currentChoiceLR = MyIndex;
					}else{
						currentChoiceLR = (int) Math.random() * 4 + 1;
						if(currentChoiceLR == MyIndex || currentChoiceLR  == TableToken){currentChoiceLR++;}
					}
					
					boolean isAllEmpty = true;
					for(int j=1;j<=6;j++){
						if(j!=TableToken){
							if(CardSet[j][0] != 0){
								isAllEmpty = false;
							}
						}
					}
					if(isAllEmpty){
						//Next State Create
						currentMsgType = GameMsg.Msg_NoOtherHaveAnEventCard;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "�ϥΥ洫�C" + "�]���S���H���ƥ�d�A���ƥ�@�o";
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					}else{
						if( CardSet[currentChoiceLR][0] == 0){
							//Next State Create
							currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "��" + Gifo.Name[CharID[currentChoiceLR]] +"�ϥΥ洫�C" + "�]���L�S���ƥ�d�A���ƥ�@�o";
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						}else{
							int retCC = (int)Math.random()*CardSet[currentChoiceLR][0] +1;
							int retCCCard = CardSet[currentChoiceLR][retCC];
							int retCCVar = CardSetVar[currentChoiceLR][retCC];
							
							int retTT = (int)Math.random()*CardSet[TableToken][0] +1;
							int retTTCard = CardSet[TableToken][retTT];
							int retTTVar = CardSetVar[TableToken][retTT];
							
							discardAnEventCard(currentChoiceLR,retCC);
							CardSet[currentChoiceLR][ ++CardSet[currentChoiceLR][0] ] = retTTCard;
							CardSetVar[currentChoiceLR][ CardSet[currentChoiceLR][0] ] = retTTVar;
							
							discardAnEventCard(TableToken,retTT);
							CardSet[TableToken][ ++CardSet[TableToken][0] ] = retCCCard;
							CardSetVar[TableToken][ CardSet[TableToken][0] ] = retCCVar;
							
							//Next State Create
							currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "��" + Gifo.Name[CharID[currentChoiceLR]] +"�ϥΥ洫�C" + 
									        Gifo.Name[CharID[TableToken]] + "�o��G" + EventCardName[retCCCard] + "; " + Gifo.Name[CharID[currentChoiceLR]] + "�o��G" + EventCardName[retTTCard] ;
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						}
					}
					break;
				case 7:
					//*
					int tttemp = CardSetVar[TableToken][currentChoiceLR];
					discardAnEventCard(TableToken, currentChoiceLR);
					
					String TempS = "";
					//
					int temp[] = new int[4] , tt ;
					temp[1] = (int) (tttemp/81);
					temp[2] = (int) ((tttemp - 81 * temp[1])/9);
					temp[3] = (int) tttemp - temp[1]*81 - temp[2]*9;
					for(int j=1;j<=3;j++){
						switch(temp[j]){
							case 1:
								if(CardSet[TableToken][0] < 9){
									getAnEventCard(TableToken);
									TempS+=  Gifo.Name[CharID[TableToken]] + "�o��:" + EventCardName[ CardSet[TableToken][CardSet[TableToken][0]] ] + ".";
									
								}else{
									TempS+=  Gifo.Name[CharID[TableToken]] +  "�w������o�d.";
								}
								
								break;
							case 2: 
								if(CardSet[TableToken][0] >= 1){
									ret = (int) Math.random() * CardSet[TableToken][0] +1;
									TempS+= Gifo.Name[CharID[TableToken]] + "���:" + EventCardName[ CardSet[TableToken][CardSet[TableToken][0]] ] + ".";
									discardAnEventCard(TableToken,ret);
								}else{
									TempS+= Gifo.Name[CharID[TableToken]] + "�w�������d.";
								}
								
								break;
							case 3:
								tt = TableToken - 1; if(tt==0){tt=6;}
								if(CardSet[tt][0] < 9){
									getAnEventCard(tt);
									TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
								}else{
									TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
								}
								break;
							case 4: 
								tt = TableToken + 1; if(tt==7){tt=1;}
								if(CardSet[tt][0] < 9){
									getAnEventCard(tt);
									TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
								}else{
									TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
								}
								break;
							case 5: 
								tt = TableToken + 3; if(tt>=7){tt-=6;}
								if(CardSet[tt][0] < 9){
									getAnEventCard(tt);
									TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
								}else{
									TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
								}
								break;
							case 6: 
								ret = (int) Math.random() * 2 +1;
								tt = 0;
								for(int i=1;i<=6;i++){
									if(CiteABC[i] == 65){
										if(tt ==0){
											tt = i;
											if( ret == 1){
												if(CardSet[tt][0] < 9){
													getAnEventCard(tt);
													TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
													}else{
														TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
												}
												break;
											}
										}else{
											tt = i;
											if(CardSet[tt][0] < 9){
												getAnEventCard(tt);
												TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
											}else{
												TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
											}
											break;
										}
									}
								}
								break;
							case 7:
								ret = (int) Math.random() * 2 +1;
								tt = 0;
								for(int i=1;i<=6;i++){
									if(CiteABC[i] == 66){
										if(tt ==0){
											tt = i;
											if( ret == 1){
												if(CardSet[tt][0] < 9){
													getAnEventCard(tt);
													TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
													}else{
														TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
												}
												break;
											}
										}else{
											tt = i;
											if(CardSet[tt][0] < 9){
												getAnEventCard(tt);
												TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
											}else{
												TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
											}
											break;
										}
									}
								}
								break;
							case 8: 
								ret = (int) Math.random() * 2 +1;
								tt = 0;
								for(int i=1;i<=6;i++){
									if(CiteABC[i] == 67){
										if(tt ==0){
											tt = i;
											if( ret == 1){
												if(CardSet[tt][0] < 9){
													getAnEventCard(tt);
													TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
												}else{
													TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
												}
												break;
											}
										}else{
											tt = i;
											if(CardSet[tt][0] < 9){
												getAnEventCard(tt);
												TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
											}else{
												TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
											}
											break;
										}
									}
								}
								break;
						}
					}
					
					//Next State Create
					currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
					MsgOptionsNumber = 1;
					MsgInitAid= new String[MsgOptionsNumber+1];
					MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "�ϥ������C" +TempS;
					MsgInitAid[1] = "�n��";
					Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					break;
				case 8:
					discardAnEventCard(TableToken,ComputerPickHandCard);
					CurrentHP[TableToken] += 2;
					if(CurrentHP[TableToken]  > Gifo.MaxHP[CharID[TableToken]]){CurrentHP[TableToken]  = Gifo.MaxHP[CharID[TableToken]];}
					break;
				case 10:
					discardAnEventCard(TableToken, currentChoiceLR);
					
					// To who?
					toYou = RamdonRate(70);
					if(toYou){
						currentChoiceLR = MyIndex;
					}else{
						currentChoiceLR = (int) Math.random() * 4 + 1;
						if(currentChoiceLR == MyIndex || currentChoiceLR  == TableToken){currentChoiceLR++;}
					}
					
					isAllEmpty = true;
					for(int i=1;i<=6;i++){
						if(i!=TableToken){
							if(CardSet[i][0] != 0){
								isAllEmpty = false;
							}
						}
					}
					if(isAllEmpty){
						//Next State Create
						currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = "�]���S���H���ƥ�d�A���ƥ�@�o";
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					}else{
						if( CardSet[currentChoiceLR][0] == 0){
							//Next State Create
							currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = "�]���L�S���ƥ�d�A���ƥ�@�o";
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						}else{
							int retCC = (int)Math.random()*CardSet[currentChoiceLR][0] +1;
							int retCCCard = CardSet[currentChoiceLR][retCC];
							discardAnEventCard(currentChoiceLR,retCC);
							CardSet[TableToken][ ++CardSet[TableToken][0] ] = retCCCard;
							
							//Next State Create
							currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "��" + Gifo.Name[CharID[currentChoiceLR]] +"�ϥεs���C"  +  Gifo.Name[CharID[TableToken]] + "�o��G" + EventCardName[retCCCard];
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						}
					}
						
					break;
				}

			}else{
				//Next State Create
				currentMsgType = GameMsg.Msg_TellWhatComputerDo_EventCard;
				MsgOptionsNumber = 1;
				MsgInitAid= new String[MsgOptionsNumber+1];
				MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "�G���ڷQ�@�Q...";
				MsgInitAid[1] = "�n��";
				Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
			}
		}else if(currentMsgType == GameMsg.Msg_TellWhatComputerDo_EventCard){
			//JOptionPane.showConfirmDialog(null, Boolean.toString(goSummonKiller), "Title", JOptionPane.YES_NO_OPTION); /**/
			
			if(goSummonKiller){
				//JOptionPane.showConfirmDialog(null, Integer.toString(2), "Title", JOptionPane.YES_NO_OPTION); /**/
				// Summon Killer
				if(CardSet[TableToken][0] >=4){
					SacrificedTotalValue = 0;
					for(int i=0;i<=9;i++){
						SacrificedCards[i] = 0;
					}
					
					for(int i=1;i<=CardSet[TableToken][0];i++){
						SacrificedCards[i] = 1;
						SacrificedTotalValue += CardValue[SacrificedCards[i]];
						if(SacrificedTotalValue >= 4){
							for(int j=1;j<=9;j++){
								if(SacrificedCards[j] == 1){
									discardAnEventCard(TableToken,j);
								}
							}
							break;
						}
						
						// To who?
						boolean toYou =RamdonRate(70);
						if(toYou){
							// To You
							Hurt(MyIndex,2);
						}else{
							int ret = (int) Math.random() * 5 + 1;
							if(ret == MyIndex){ret++;}
							Hurt(ret,2);
						}
						
						//Next State Create
						currentMsgType = GameMsg.Msg_TellWhatComputerDo_SummonKiller;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = Gifo.Name[CharID[TableToken]]; 
						if(toYou){ MsgInitAid[0]+="��A�o�ʼf�P�̧���!!"; } else { MsgInitAid[0]+="��"+ Gifo.Name[CharID[currentChoiceLR]] +"�o�ʼf�P�̧���!!"; }
						MsgInitAid[1] = "�n��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
					}
				}
			}else{
				//JOptionPane.showConfirmDialog(null, "3LI", "Title", JOptionPane.YES_NO_OPTION); /**/
				//Next State Create
				currentMsgType = GameMsg.Msg_TellWhatComputerDo_SummonKiller;
				MsgOptionsNumber = 1;
				MsgInitAid= new String[MsgOptionsNumber+1];
				MsgInitAid[0] = Gifo.Name[CharID[TableToken]] + "�G�A�Ӯz�F!!";
				MsgInitAid[1] = "�n��";
				Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
			}
		
		}else if(currentMsgType == GameMsg.Msg_TellWhatComputerDo_SummonKiller){
			// End of Computer
			if(TableToken <=5){
				TableToken++; 
				Skill1Used = false; Skill2Used = false;
				//Next State Create
				currentMsgType = GameMsg.Msg_RoundingARound;
				MsgOptionsNumber = 1;
				MsgInitAid= new String[MsgOptionsNumber+1];
				MsgInitAid[0] = "����" + Gifo.Name[CharID[TableToken]] + "!";
				MsgInitAid[1] = "�n��";
				Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
			}else{
				TableToken=1;
				//Next State Create
				currentMsgType = GameMsg.Msg_RoundEnd;
				MsgOptionsNumber = 1;
				MsgInitAid= new String[MsgOptionsNumber+1];
				MsgInitAid[0] = "���^�X����!!";
				MsgInitAid[1] = "�n��";
				Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
			}
			
		}else if(currentMsgType == GameMsg.Msg_ReadImformation){
			// Do nothing.
			
		}else if(currentMsgType == GameMsg.Msg_ExitRequest){
			
			if(currentChoiceUD == 1){
				System.exit(0);
			}else{
				currentChoiceUD = 1;
				currentMsgType = MsgTypeTempforExit;
			}
			
		}else if(currentMsgType == GameMsg.Msg_SummonKiller){
			switch(currentChoiceUD){
				case 1:
					if(SacrificedCards[currentChoiceLR] == 0){
						SacrificedCards[currentChoiceLR] = 1;
						SacrificedCards[0]++;
						SacrificedTotalValue += CardValue[CardSet[TableToken][currentChoiceLR]];
					}else{
						SacrificedCards[currentChoiceLR] = 0;
						SacrificedCards[0]--;
						SacrificedTotalValue -= CardValue[CardSet[TableToken][currentChoiceLR]];
					}
					break;
				case 2:
					if(SacrificedTotalValue >= 4){
						for(int i=1;i<=9;i++){
							if(SacrificedCards[i] == 1){
								discardAnEventCard(TableToken,i);
							}
						}
						
						WhoAsAim_parameter = 0;
						//Next State Create
						currentMsgType = GameMsg.Msg_WhoAsAim;
						MsgOptionsNumber = 1;
						MsgInitAid= new String[MsgOptionsNumber+1];
						MsgInitAid[0] = "���k���w�@��ؼ�";
						MsgInitAid[1] = "�M�w��";
						Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						
					}else{
						ShowOnYouCantKill = true;
					}
					
					currentChoiceLR = 1; 
					currentChoiceUD = 1;
					SacrificedTotalValue = 0;
					for(int i=0;i<=9;i++){
						SacrificedCards[i] = 0;
					}
					break;
				case 3:
					currentChoiceLR = 1; 
					currentChoiceUD = 4;
					currentMsgType = GameMsg.Msg_MyTurn;
					break;
			}
		}else if(currentMsgType == GameMsg.Msg_WhoAsAim){

			if(currentChoiceLR == TableToken){
				ShowOnYouCantKill = true;
			}else{
				boolean isAllEmpty;
				
				switch(WhoAsAim_parameter){
					case 0:		// Summon Killer
						Hurt(currentChoiceLR, 2);
						currentMsgType = GameMsg.Msg_MyTurn;
						break;
					case 1:
						if(CardSet[currentChoiceLR][0] >= 9){
							//Next State Create
							currentMsgType = GameMsg.Msg_NoOtherHaveAnEventCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = "�]���ؼЪ���P�w�F�̦h���E�i�A���ƥ�@�o";
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);							
						}else{
							++CardSet[currentChoiceLR][0];
							CardSet[currentChoiceLR][CardSet[currentChoiceLR][0]] = 1;
							CardSetVar[currentChoiceLR][CardSet[currentChoiceLR][0]] = WhoAsAim_TTVar;
							
							//Next State Create
							currentMsgType = GameMsg.Msg_GetWhatCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							
														
							if(WhoAsAim_TTVar<=3) {
								
								if(CiteABC[currentChoiceLR] == WhoAsAim_TTVar + 64){
									int ret = (int) Math.random() * CardSet[currentChoiceLR][0] + 1;
									discardAnEventCard(currentChoiceLR,ret);
									MsgInitAid[0] = "�L�����d�A��@�i�P!";
								}else{
									MsgInitAid[0] = "�L�q�q�����d�A�S���R!";
								}
								
							}else{
								WhoAsAim_TTVar-=3;
								if(CiteABC[currentChoiceLR] == WhoAsAim_TTVar + 64){
									getAnEventCard(currentChoiceLR);
									MsgInitAid[0] = "�L�����d�A��@�i�P!";
								}else{
									MsgInitAid[0] = "�L�q�q�����d�A�S���R!";
								}
							}
							
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						}
						break;
						
					case 6:
						isAllEmpty = true;
						for(int i=1;i<=6;i++){
							if(i!=TableToken){
								if(CardSet[i][0] != 0){
									isAllEmpty = false;
								}
							}
						}
						if(isAllEmpty){
							//Next State Create
							currentMsgType = GameMsg.Msg_NoOtherHaveAnEventCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = "�]���S���H���ƥ�d�A���ƥ�@�o";
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						}else{
							if( CardSet[currentChoiceLR][0] == 0){
								//Next State Create
								currentMsgType = GameMsg.Msg_NoOtherHaveAnEventCard;
								MsgOptionsNumber = 1;
								MsgInitAid= new String[MsgOptionsNumber+1];
								MsgInitAid[0] = "�]���L�S���ƥ�d�A���ƥ�@�o";
								MsgInitAid[1] = "�n��";
								Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
							}else{
								int retCC = (int)Math.random()*CardSet[currentChoiceLR][0] +1;
								int retCCCard = CardSet[currentChoiceLR][retCC];
								int retCCVar = CardSetVar[currentChoiceLR][retCC];
								
								int retTT = (int)Math.random()*CardSet[TableToken][0] +1;
								int retTTCard = CardSet[TableToken][retTT];
								int retTTVar = CardSetVar[TableToken][retTT];
								
								discardAnEventCard(currentChoiceLR,retCC);
								CardSet[currentChoiceLR][ ++CardSet[currentChoiceLR][0] ] = retTTCard;
								CardSetVar[currentChoiceLR][ CardSet[currentChoiceLR][0] ] = retTTVar;
								
								discardAnEventCard(TableToken,retTT);
								CardSet[TableToken][ ++CardSet[TableToken][0] ] = retCCCard;
								CardSetVar[TableToken][ CardSet[TableToken][0] ] = retCCVar;
								
								//Next State Create
								currentMsgType = GameMsg.Msg_GetWhatCard;
								MsgOptionsNumber = 1;
								MsgInitAid= new String[MsgOptionsNumber+1];
								MsgInitAid[0] = "�A�o��G" + EventCardName[retCCCard] + "; �L�o��G" + EventCardName[retTTCard] ;
								MsgInitAid[1] = "�n��";
								Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
							}
						}
						break;
					case 10:
						isAllEmpty = true;
						for(int i=1;i<=6;i++){
							if(i!=TableToken){
								if(CardSet[i][0] != 0){
									isAllEmpty = false;
								}
							}
						}
						if(isAllEmpty){
							//Next State Create
							currentMsgType = GameMsg.Msg_NoOtherHaveAnEventCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = "�]���S���H���ƥ�d�A���ƥ�@�o";
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
						}else{
							if( CardSet[currentChoiceLR][0] == 0){
								//Next State Create
								currentMsgType = GameMsg.Msg_NoOtherHaveAnEventCard;
								MsgOptionsNumber = 1;
								MsgInitAid= new String[MsgOptionsNumber+1];
								MsgInitAid[0] = "�]���L�S���ƥ�d�A���ƥ�@�o";
								MsgInitAid[1] = "�n��";
								Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
							}else{
								int retCC = (int)Math.random()*CardSet[currentChoiceLR][0] +1;
								int retCCCard = CardSet[currentChoiceLR][retCC];
								discardAnEventCard(currentChoiceLR,retCC);
								CardSet[TableToken][ ++CardSet[TableToken][0] ] = retCCCard;
								
								//Next State Create
								currentMsgType = GameMsg.Msg_GetWhatCard;
								MsgOptionsNumber = 1;
								MsgInitAid= new String[MsgOptionsNumber+1];
								MsgInitAid[0] = "�A�o��G" + EventCardName[retCCCard];
								MsgInitAid[1] = "�n��";
								Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
							}
						}

						
				}
				
				currentChoiceLR = 1; 
				currentChoiceUD = 1;
			}
			

		}else if(currentMsgType == GameMsg.Msg_PickAnEventCard){
			switch(currentChoiceUD){
				case 1:
					switch(CardSet[TableToken][currentChoiceLR]){
						case 1:
							WhoAsAim_TTVar = CardSetVar[TableToken][currentChoiceLR];
							WhoAsAim_parameter = 1;
							discardAnEventCard(TableToken, currentChoiceLR);
							
							//Next State Create
							currentMsgType = GameMsg.Msg_WhoAsAim;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = "���k���w�@��ؼ�";
							MsgInitAid[1] = "�M�w��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
							
							
							break;
						
						case 4:
							discardAnEventCard(TableToken, currentChoiceLR);
							
							for(int i=1;i<=6;i++){
								if(i!=TableToken){
									Hurt(i,2);
								}
							}
							break;
							
						case 5:
							discardAnEventCard(TableToken, currentChoiceLR);
							
							getAnEventCard(TableToken);
							getAnEventCard(TableToken);
							break;
							
						case 6:
							if(CardSet[TableToken][0] >= 2){
								discardAnEventCard(TableToken, currentChoiceLR);
								
								WhoAsAim_parameter = 6;
								//Next State Create
								currentMsgType = GameMsg.Msg_WhoAsAim;
								MsgOptionsNumber = 1;
								MsgInitAid= new String[MsgOptionsNumber+1];
								MsgInitAid[0] = "���k���w�@��ؼ�";
								MsgInitAid[1] = "�M�w��";
								Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
							}
							break;
						case 7:
							int tttemp = CardSetVar[TableToken][currentChoiceLR];
							//JOptionPane.showConfirmDialog(null, tttemp, "1", JOptionPane.YES_NO_OPTION);
							discardAnEventCard(TableToken, currentChoiceLR);
							
							
							String TempS = "";
							//
							int temp[] = new int[4] , tt , ret;
							temp[1] = (int) (tttemp/81);
							temp[2] = (int) ((tttemp - 81 * temp[1])/9);
							temp[3] = (int) tttemp - temp[1]*81 - temp[2]*9;
							for(int j=1;j<=3;j++){
								switch(temp[j]){
									case 1:
										if(CardSet[TableToken][0] < 9){
											getAnEventCard(TableToken);
											TempS+= "�ڱo��:" + EventCardName[ CardSet[TableToken][CardSet[TableToken][0]] ] + ".";
											
										}else{
											TempS+= "�ڤw������o�d.";
										}
										
										break;
									case 2: 
										if(CardSet[TableToken][0] >= 1){
											ret = (int) Math.random() * CardSet[TableToken][0] +1;
											TempS+= "�ک��:" + EventCardName[ CardSet[TableToken][CardSet[TableToken][0]] ] + ".";
											discardAnEventCard(TableToken,ret);
										}else{
											TempS+= "�ڤw�������d.";
										}
										
										break;
									case 3:
										tt = TableToken - 1; if(tt==0){tt=6;}
										if(CardSet[tt][0] < 9){
											getAnEventCard(tt);
											TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
										}else{
											TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
										}
										break;
									case 4: 
										tt = TableToken + 1; if(tt==7){tt=1;}
										if(CardSet[tt][0] < 9){
											getAnEventCard(tt);
											TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
										}else{
											TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
										}
										break;
									case 5: 
										tt = TableToken + 3; if(tt>=7){tt-=6;}
										if(CardSet[tt][0] < 9){
											getAnEventCard(tt);
											TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
										}else{
											TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
										}
										break;
									case 6: 
										ret = (int) Math.random() * 2 +1;
										tt = 0;
										for(int i=1;i<=6;i++){
											if(CiteABC[i] == 65){
												if(tt ==0){
													tt = i;
													if( ret == 1){
														if(CardSet[tt][0] < 9){
															getAnEventCard(tt);
															TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
															}else{
																TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
														}
														break;
													}
												}else{
													tt = i;
													if(CardSet[tt][0] < 9){
														getAnEventCard(tt);
														TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
													}else{
														TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
													}
													break;
												}
											}
										}
										break;
									case 7:
										ret = (int) Math.random() * 2 +1;
										tt = 0;
										for(int i=1;i<=6;i++){
											if(CiteABC[i] == 66){
												if(tt ==0){
													tt = i;
													if( ret == 1){
														if(CardSet[tt][0] < 9){
															getAnEventCard(tt);
															TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
															}else{
																TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
														}
														break;
													}
												}else{
													tt = i;
													if(CardSet[tt][0] < 9){
														getAnEventCard(tt);
														TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
													}else{
														TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
													}
													break;
												}
											}
										}
										break;
									case 8: 
										ret = (int) Math.random() * 2 +1;
										tt = 0;
										for(int i=1;i<=6;i++){
											if(CiteABC[i] == 67){
												if(tt ==0){
													tt = i;
													if( ret == 1){
														if(CardSet[tt][0] < 9){
															getAnEventCard(tt);
															TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
														}else{
															TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
														}
														break;
													}
												}else{
													tt = i;
													if(CardSet[tt][0] < 9){
														getAnEventCard(tt);
														TempS+= Gifo.Name[CharID[tt]] + "�o��:" + EventCardName[ CardSet[tt][CardSet[tt][0]] ] + ".";
													}else{
														TempS+= Gifo.Name[CharID[tt]] + "�w������o�d.";
													}
													break;
												}
											}
										}
										break;
								}
							}
							//Next State Create
							currentMsgType = GameMsg.Msg_GetWhatCard;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = TempS;
							MsgInitAid[1] = "�n��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
							break;
						case 8:
							discardAnEventCard(TableToken, currentChoiceLR);
							
							CurrentHP[TableToken] += 2;
							if(CurrentHP[TableToken]  > Gifo.MaxHP[CharID[TableToken]]){CurrentHP[TableToken]  = Gifo.MaxHP[CharID[TableToken]];}
							break;
						case 10:
							discardAnEventCard(TableToken, currentChoiceLR);
							
							WhoAsAim_parameter = 10;
							//Next State Create
							currentMsgType = GameMsg.Msg_WhoAsAim;
							MsgOptionsNumber = 1;
							MsgInitAid= new String[MsgOptionsNumber+1];
							MsgInitAid[0] = "���k���w�@��ؼ�";
							MsgInitAid[1] = "�M�w��";
							Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
							break;
						
						case 2:
						case 3:
						case 9:
							break;	
					}
					
					currentChoiceLR = 1;
					break;
					
				case 2:
					currentChoiceLR = 1; 
					currentChoiceUD = 3;
					currentMsgType = GameMsg.Msg_MyTurn;
					break;
			}
			
			
		}else if(currentMsgType == GameMsg.Msg_NoOtherHaveAnEventCard){
			currentChoiceLR = 1; 
			currentChoiceUD = 1;
			currentMsgType = GameMsg.Msg_MyTurn;
			
		}else if(currentMsgType == GameMsg.Msg_GetWhatCard){
			currentChoiceLR = 1; 
			currentChoiceUD = 1;
			currentMsgType = GameMsg.Msg_MyTurn;
		}else if(currentMsgType == GameMsg.Msg_RoundEnd){
			//Next State Create
			currentMsgType = GameMsg.Msg_RoundStart;
			MsgOptionsNumber = 1;
			MsgInitAid= new String[MsgOptionsNumber+1];
			MsgInitAid[0] = "��"+ Integer.toString(currentRound+1)+"�^�X�n�}�l�o!";
			MsgInitAid[1] = "�n��";
			Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
		}
		

	}
	
	public void keyPressed(int k) {
		// Specials
		ShowOnYouCantKill = false;
		CantUsedThisSpellStringStatement = "";
		
		
		
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		
		if(k == KeyEvent.VK_UP) {
			if( currentChoiceUD != 1){
				currentChoiceUD--;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			
			if( currentChoiceUD != Msg[currentMsgType].getOptionsNumber()){
				currentChoiceUD++;
			}
		}
		if(k == KeyEvent.VK_LEFT) {
			if(currentMsgType == GameMsg.Msg_ReadImformation || currentMsgType ==  GameMsg.Msg_WhoAsAim || currentMsgType ==  GameMsg.Msg_SelectTarget){
				if(currentChoiceLR==6){
					currentChoiceLR=1;
				}else{
					currentChoiceLR++;
				}
			}
			if(currentMsgType == GameMsg.Msg_SummonKiller  || currentMsgType ==  GameMsg.Msg_PickAnEventCard || currentMsgType == GameMsg.Msg_SelectEventCards){
				if(currentChoiceLR > 1){
					currentChoiceLR--;
				}
			}
		}
		if(k == KeyEvent.VK_RIGHT) {
			if(currentMsgType == GameMsg.Msg_ReadImformation || currentMsgType ==  GameMsg.Msg_WhoAsAim || currentMsgType ==  GameMsg.Msg_SelectTarget){
				if(currentChoiceLR==1){
					currentChoiceLR=6;
				}else{
					currentChoiceLR--;
				}
			}
			if(currentMsgType == GameMsg.Msg_SummonKiller || currentMsgType == GameMsg.Msg_PickAnEventCard || currentMsgType == GameMsg.Msg_SelectEventCards){
				if(currentChoiceLR <= CardSet[TableToken][0] - 1){
					currentChoiceLR++;
				}
			}
			
		}
		if(k == KeyEvent.VK_ESCAPE){
			if(currentMsgType == GameMsg.Msg_ReadImformation){
				currentMsgType = GameMsg.Msg_MyTurn;
			}else{
				currentChoiceUD = 1;
				MsgTypeTempforExit = currentMsgType;
				//Next State Create
				currentMsgType = GameMsg.Msg_ExitRequest;
				MsgOptionsNumber = 2;
				MsgInitAid= new String[MsgOptionsNumber+1];
				MsgInitAid[0] = "���h?";
				MsgInitAid[1] = "�n��"; 
				MsgInitAid[2] = "���n";
				Msg[currentMsgType] = new GameMsg(MsgOptionsNumber,MsgInitAid);
			}
			
		}
		
		
	}
	
	public void keyReleased(int k) {}
	
}