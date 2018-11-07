package TileMap;

import Main.GamePanel;

import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class GameMsg {
	private String Statement;
	private int OptionsNumber;
	private String Options[];
	
	public static final int Msg_RoundStart = 1;
	public static final int Msg_RoundStartSkillExcludeYou = 2;
	public static final int Msg_RoundStartSkillComputerShow = 3;
	public static final int Msg_YourRoundStartSkillRequest = 4;
	public static final int Msg_RoundingARound = 5;
	public static final int Msg_MyTurn = 6;
	public static final int Msg_ComputersTurn = 7;
	public static final int Msg_ReadImformation = 8;
	public static final int Msg_ExitRequest = 9;
	public static final int Msg_SummonKiller = 10;
	public static final int Msg_WhoAsAim = 11;
	public static final int Msg_PickAnEventCard =12;
	public static final int Msg_NoOtherHaveAnEventCard = 13;
	public static final int Msg_GetWhatCard = 14;
	public static final int Msg_RoundEnd = 15;
	public static final int Msg_TellWhatComputerDo_EventCard = 16;
	public static final int Msg_TellWhatComputerDo_SummonKiller = 17;
	public static final int Msg_ActiveSpillShow = 18;
	public static final int Msg_SelectEventCards = 19;
	public static final int Msg_SelectTarget = 20;
	
	public int getOptionsNumber(){return OptionsNumber;}
	public String getStatement(){return Statement;}
	public String getOptions(int Index){return Options[Index];}

	public GameMsg(int optionsNumber, String options[]){
		Statement =  options[0];
		OptionsNumber = optionsNumber;
		Options = new String[optionsNumber+1];
		for(int i=1;i<=OptionsNumber;i++){
			Options[i] = options[i];
		}

	}
}
