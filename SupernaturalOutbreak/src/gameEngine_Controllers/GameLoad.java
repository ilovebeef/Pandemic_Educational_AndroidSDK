/**
 * Created and Implemented by: Hong Nguyen
**/
package gameEngine_Controllers;

import gameEngine_frameworkView.GameGraphic_interfaceView;
import gameEngine_frameworkView.GameScreen_View;
import gameEngine_frameworkView.Game_interfaceView;
import gameEngine_frameworkView.GameGraphic_interfaceView.PixmapFormat;

public class GameLoad extends GameScreen_View {
	
	GameGraphic_interfaceView gameGraphic;
	
	public GameLoad (Game_interfaceView game) {
		super (game);
		gameGraphic = game.getGraphics();
		System.gc();
	}
	
	public void update(float deltaTime) {
		System.gc();
        	//Load our screen assets
        GameAssets.menuScreen = gameGraphic.newPixmap ("Menu_Images/menu_screen.png", PixmapFormat.RGB565);
        GameAssets.creditScreen = gameGraphic.newPixmap ("Menu_Images/game_credit.png", PixmapFormat.RGB565);
        GameAssets.helpScreen = gameGraphic.newPixmap ("Menu_Images/game_help.png", PixmapFormat.RGB565);
        GameAssets.pandemic_directions = gameGraphic.newPixmap("Menu_Images/pandemic_directions.png", PixmapFormat.RGB565);
    	GameAssets.pandemic_directions2 = gameGraphic.newPixmap("Menu_Images/pandemic_directions2.png", PixmapFormat.RGB565);
        GameAssets.settingScreen = gameGraphic.newPixmap ("Menu_Images/bg_blank.png", PixmapFormat.RGB565);
        	//Load our button assets
        GameAssets.creditButton = gameGraphic.newPixmap("Menu_Images/credit_icon1.png", PixmapFormat.ARGB4444);
        GameAssets.helpButton = gameGraphic.newPixmap("Menu_Images/help_icon1.png", PixmapFormat.ARGB4444);
        GameAssets.playButton = gameGraphic.newPixmap("Menu_Images/play_icon1.png", PixmapFormat.ARGB4444);
        GameAssets.backButton = gameGraphic.newPixmap("Menu_Images/back_icon1.png", PixmapFormat.ARGB4444);
        GameAssets.nextButton = gameGraphic.newPixmap("Menu_Images/next_icon1.png", PixmapFormat.ARGB4444);
        	//Load G.U.I assets
        GameAssets.radioChecked = gameGraphic.newPixmap("Menu_Images/radio_checked1.png", PixmapFormat.ARGB4444);
        GameAssets.radioUnchecked = gameGraphic.newPixmap("Menu_Images/radio_unchecked1.png", PixmapFormat.ARGB4444);
        	//Loads the Map
        GameAssets.gameMap = gameGraphic.newPixmap("In_Game/board_0.png", PixmapFormat.RGB565);
    		//Loads game map's assets
        GameAssets.scroll_Xaxis = gameGraphic.newPixmap ("In_Game/scroll_Xaxis2.png", PixmapFormat.ARGB4444);
        GameAssets.scroll_Yaxis = gameGraphic.newPixmap("In_Game/scroll_Yaxis2.png", PixmapFormat.ARGB4444);
        GameAssets.scroll_Ball = gameGraphic.newPixmap("In_Game/scroll_Ball2.png", PixmapFormat.ARGB4444);
        GameAssets.gameMap_statsunderline = gameGraphic.newPixmap("In_Game/gameMenu_ratesunderline.png", PixmapFormat.ARGB4444);
        GameAssets.gameMap_skullhead = gameGraphic.newPixmap("In_Game/gameMenu_Skullhead.png",PixmapFormat.ARGB4444);
        GameAssets.actionbar = gameGraphic.newPixmap("In_Game/actionbar2.png", PixmapFormat.ARGB4444);
        GameAssets.researchStation = gameGraphic.newPixmap("In_Game/research-station-32.png", PixmapFormat.ARGB4444);
        GameAssets.travelring = gameGraphic.newPixmap("In_Game/travelring.png", PixmapFormat.ARGB4444);
        GameAssets.giveCardButton = gameGraphic.newPixmap("In_Game/givecard.png", PixmapFormat.ARGB4444);
        GameAssets.takeCardButton = gameGraphic.newPixmap("In_Game/takecard.png", PixmapFormat.ARGB4444);
        GameAssets.cancelButton = gameGraphic.newPixmap("In_Game/cancel.png", PixmapFormat.ARGB4444);
        GameAssets.cancelVertical = gameGraphic.newPixmap("In_Game/cancelvertical.png", PixmapFormat.ARGB4444);
        GameAssets.actionBackdrop = gameGraphic.newPixmap("In_Game/backdrop.png", PixmapFormat.ARGB4444);
        GameAssets.currentPlayer = gameGraphic.newPixmap("In_Game/currentplayer.png", PixmapFormat.ARGB4444);
        GameAssets.playerCardBack = gameGraphic.newPixmap("In_Game/player_card_back.png", PixmapFormat.ARGB4444);
        GameAssets.infectionCardBack = gameGraphic.newPixmap("In_Game/infection_card_back.png", PixmapFormat.ARGB4444);
        GameAssets.cureBlack = gameGraphic.newPixmap("In_Game/CureBlack.png", PixmapFormat.ARGB4444);
        GameAssets.cureBlue = gameGraphic.newPixmap("In_Game/CureBlue.png", PixmapFormat.ARGB4444);
        GameAssets.cureRed = gameGraphic.newPixmap("In_Game/CureRed.png", PixmapFormat.ARGB4444);
        GameAssets.cureYellow = gameGraphic.newPixmap("In_Game/CureYellow.png", PixmapFormat.ARGB4444);
        GameAssets.frameBlack = gameGraphic.newPixmap("In_Game/Frame_Black.png", PixmapFormat.ARGB4444);
        GameAssets.frameBlue = gameGraphic.newPixmap("In_Game/Frame_Blue.png", PixmapFormat.ARGB4444);
        GameAssets.frameRed = gameGraphic.newPixmap("In_Game/Frame_Red.png", PixmapFormat.ARGB4444);
        GameAssets.frameYellow = gameGraphic.newPixmap("In_Game/Frame_Yellow.png", PixmapFormat.ARGB4444);
        GameAssets.dispatcherPawnJoin = gameGraphic.newPixmap("In_Game/dispatcherpawnmove1.png", PixmapFormat.ARGB4444);
        GameAssets.dispatcherPawnControl = gameGraphic.newPixmap("In_Game/dispatcherpawnmove2.png", PixmapFormat.ARGB4444);
        GameAssets.dispatcherControlPanel = gameGraphic.newPixmap("In_Game/dispatchercontrol.png", PixmapFormat.ARGB4444);
        GameAssets.dispatcherControlCancel = gameGraphic.newPixmap("In_Game/dispatchercancel.png", PixmapFormat.ARGB4444);
        GameAssets.yesButton = gameGraphic.newPixmap("In_Game/yes.png", PixmapFormat.ARGB4444);
        GameAssets.actionArrowLeft = gameGraphic.newPixmap("In_Game/arrowleft.png", PixmapFormat.ARGB4444);
        GameAssets.actionArrowRight = gameGraphic.newPixmap("In_Game/arrowright.png", PixmapFormat.ARGB4444);
        	// Loads cube assets
        GameAssets.mainTabs = gameGraphic.newPixmap("In_Game/tabsMenu.png", PixmapFormat.ARGB4444);
        GameAssets.blueCube = gameGraphic.newPixmap("Cubes/blueCube_0.png", PixmapFormat.ARGB4444);
        GameAssets.bigBlueCube = gameGraphic.newPixmap("Cubes/blueCube_1.png", PixmapFormat.ARGB4444);
        GameAssets.blackCube = gameGraphic.newPixmap("Cubes/blackCube_0.png", PixmapFormat.ARGB4444);
        GameAssets.bigBlackCube = gameGraphic.newPixmap("Cubes/blackCube_1.png", PixmapFormat.ARGB4444);
        GameAssets.redCube = gameGraphic.newPixmap("Cubes/redCube_0.png", PixmapFormat.ARGB4444);
        GameAssets.bigRedCube = gameGraphic.newPixmap("Cubes/redCube_1.png", PixmapFormat.ARGB4444);
        GameAssets.yellowCube = gameGraphic.newPixmap("Cubes/yellowCube_0.png", PixmapFormat.ARGB4444);
        GameAssets.bigYellowCube = gameGraphic.newPixmap("Cubes/yellowCube_1.png", PixmapFormat.ARGB4444);
        	//Loads music 
        //GameAssets.mainMenu_BGM = game.getAudio().newMusic("Audio/mainMenu_BGM.mp3");
        //GameAssets.gameMap_BGM = game.getAudio().newMusic("Audio/gameMap_BGM.mp3");
        //GameAssets.warriorDrums = game.getAudio().newMusic("Audio/warriordrums.mp3");
        	//Loads SFX
        GameAssets.mainMenu_Click = game.getAudio().newSound("Audio/mainMenu_Click.mp3");
        GameAssets.settings_Click = game.getAudio().newSound("Audio/beep.wav");
        
        game.setScreen(new GameMainMenu(game));
    }
    
    public void present(float deltaTime) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {

    }
}
