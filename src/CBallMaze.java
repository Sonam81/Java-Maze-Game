


/** These codes are used to import built in library functions.
 * JFrame,JPanel,JButton etc uses java.swing.*
 * Color,Container,LineBorder etc uses java.awt.*
 * Random Number generator uses java.util.random etc
 * Sound file uses java.sound,sampled.*
 */
import java.awt.*;
import java.io.*;     
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;


/**
 * Creating new Class named CBallMaze 
 * Extending class to JFrame, a process of Inheritance
 * Implementing ActionListener to perform click events
 */
public class CBallMaze extends JFrame implements ActionListener
{
	/**
	 * Private and public are access modifier
	 */
	private JPanel jPanelMaze, jPanelSide, jPanelLow;		//Creating instance of JPanel
	private JLabel jLOption, jLSquare, jLDirection,jLPoint;	//Creating instance of JLabel

	private JMenuBar mBar;
	private JMenu menuScenario, menuEdit,menuControls,menuHelp;
	private JMenuItem iScenarioOne,iControlsOne,iControlsTwo,iControlsThree,iHelpAbout;

	private JTextField jTOption, jTSquare, jTDirection,jTPoint;	
	private JLabel jLTimer, jLDot1, jLDot2;  
	private JTextField jTHour, jTMinute, jTSecond;  

	private JButton jBUp, jBLeft, jBDown, jBRight, jBBlankOne, jBBlankTwo,
					jBBlankThree, jBBlankFour, jBBlankFive; 


	private JButton jBOptOne, jBOptTwo, jBOptThree, jBExit;
 
	private JButton jBCompass;  
	private ImageIcon iCompass; 

	private JButton jBAct, jBRun, jBReset;

	private JLabel jLSpeed,jLInfo;  
	private JSlider jSSpeed; 

	private ImageIcon iSand, iWhite, iSandStone, iBall, iBallE, iGoldBall,iCoin,
					  iEast, iWest, iNorth, iSouth;  //creating instance of JPanel
	private JLabel jLSand[][]; 

	private ImageIcon iconRun, iconAct, iconReset;
     
	private Random rNumber;     

	private int bRow = 0,bCol = 15;   // Initial Position of Ball
	private int pointL = 0;           // Initial point on point Text Field

	private Timer fall, dWatch ;     
	private int count = 0;			  

	/**
	 * @param args
	 * Creating main method, the first command that runs in program
	 * All the features of application within in main Method
	 * This method calls different functions like createGui,autoMoveDown etc.
	 */
	public static void main(String[] args) 
	{											
		CBallMaze frame = new CBallMaze();		//Creating new object frame
		frame.setResizable(false);				//Disabling resizable feature
		frame.setSize(775,650);					//Set size to 775x650px
		frame.createGui();						// Calling createGui method
		frame.setLocationRelativeTo(null);		// Centering the position of frame
		frame.setIconImage(new ImageIcon("images/gold-ball.png").getImage()); 
												// Set icon of the application in windows
		frame.setVisible(true);					// Making frame visible
		frame.setTitle("CBallMaze - Ball Maze Application"); // Title of Frame
		frame.autoMoveDown();					// Calling auto fall method
	}

	/**
	 * Creating method createGui
	 * All the Gui for the game are added in this method 
	 */
	public void createGui()
	{				
		setDefaultCloseOperation(EXIT_ON_CLOSE);// Closes the frame on exit
		Container c = getContentPane();			// creating new container for frame
		c.setLayout(null);						// set layout to null, use setBound to set position

	/**
	 * Adding menubar menus and menuItem in the frame
	 * .setBound function will set x position,y position, width and height of the object
	 */
		mBar = new JMenuBar(); 					
		mBar.setBounds(6,0,759,20);					
		menuScenario = new JMenu("Scenario"); 	
		menuEdit = new JMenu("Edit"); 			
		menuControls = new JMenu("Controls");	
		menuHelp = new JMenu("Help"); 			
		iHelpAbout = new JMenuItem("About");	

		iHelpAbout.addActionListener(new ActionListener() 
		{ // Funtions after clicking Menu Item About

			public void actionPerformed(ActionEvent arg0) 
			{
				JOptionPane.showMessageDialog(null, " OBJECTIVE: Roll ball from top right corner to the Goal position in buttom left corner.\n" +
						" Follow the instrucion to complete the game.\n1." +
						" 1.Click Run button to Run the game.\n" +
						" 2.Click Reset Button to Reset the game.\n" +
						" 3.Click Option 1 to control using the buttons with no auto fall.\n 4." +
						" 4.Click Option 2 to control using keyboard keys with auto fall.\n " +
						" 5. Use Enemy Button to summon enemy into the maze.\n " +
						" 6. You will lose if the ball touches the enemy.\n7. Press Exit Butoon to exit the game.");
				// Displays a Message Dialoge
			}
		});


		iScenarioOne = new JMenuItem("Exit"); 	// creating exit menu item
		iScenarioOne.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);				
			}
		});
		iControlsOne = new JMenuItem("Act");	// creating act menu item
		iControlsOne.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent e) 
			{
				moveLeft();						// calling moveLeft one every one click
				fall.start();					// start auto fall function

			}
		});
		iControlsTwo = new JMenuItem("Run");	// creating run menu item
		iControlsTwo.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				jLInfo.setText("ENJOY THE GAME :)");
				dWatch.start();     		//start digital watch
				jBOptOne.setEnabled(true);	// enable function of Option1
				jBOptTwo.setEnabled(true);	// enable function of Option2
				jBOptThree.setEnabled(true);// enable function of Option3
			}
		});
		iControlsThree= new JMenuItem("Reset");// creating reset menu item
		iControlsThree.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				reset();					// calling reset method
			}
		});
		menuScenario.add(iScenarioOne);	

		menuControls.add(iControlsOne);	
		menuControls.add(iControlsTwo); 
		menuControls.add(iControlsThree);
		menuHelp.add(iHelpAbout);		
		mBar.add(menuScenario); 		
		mBar.add(menuEdit);					
		mBar.add(menuControls);				
		mBar.add(menuHelp);				
		c.add(mBar);						


		//panelMaze and its features
		jPanelMaze = new JPanel();						//Creating new Panel
		jPanelMaze.setLayout(new GridLayout(13,16));	//Using GridLayout to set maze
		jPanelMaze.setBounds(6,20, 590, 488);			//Setting xPos, yPos, width and Height
		jPanelMaze.setBackground(new Color(245,245,245));// Background Color in RGB
		jPanelMaze.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		c.add(jPanelMaze);								//Adding maze to container

		/*---------------------------------Creating imageIcon ----------------------------*/
		iEast = new ImageIcon("images/east.jpg");
		iNorth = new ImageIcon("images/north.jpg");
		iSouth = new ImageIcon("images/south.jpg");
		iWest = new ImageIcon("images/west.jpg");
		iSand = new ImageIcon("images/sand.jpg");
		iBall = new ImageIcon("images/sand60x60.png");
		iBallE = new ImageIcon("images/BallE.jpg");
		iWhite = new ImageIcon("images/white32x32.jpg");
		iSandStone = new ImageIcon("images/sandstone.jpg");
		iGoldBall = new ImageIcon("images/gold-ball.png");
		iCoin = new ImageIcon("images/coin.jpg");
		/*---------------------------------Creating imageIcon ----------------------------*/

		jLSand= new JLabel[13][16];				//2D JLabel Array
		//Nested For Loop 
		for (int i = 0; i < 13; i++)	// for loop to set vertical sand path	
		{
			for (int j = 0; j < 16; j++)// for loop to set horizontal sand path
			{
				jLSand[i][j] = new JLabel();
				jLSand[i][j].setIcon(iWhite); //set icon of JLabel to iWhite
				jPanelMaze.add(jLSand[i][j]); //adding the jLabel with icon in container

				if(i == 0 && j == 15 || i == 3 && j ==15 || i == 6 && j ==15|| i == 9 && j ==15){
					// i is for row and j is for column
					for(j = 0;j<16;j++){ 
					// for the above condition set icon in given position
						jLSand[i][j].setIcon(iSand);
						jLSand[0][15].setIcon(iBall);
					}	
				}

				else if(i == 1 && j == 15 || i == 2 && j == 15){
					// for the above condition set icon in given position
					jLSand[i][1].setIcon(iSand);
					jLSand[i][5].setIcon(iSand);
					jLSand[i][9].setIcon(iSand);
				}

				else if(i == 4 && j ==15 || i == 5 && j ==15){
					// for the above condition set icon in given position
					jLSand[i][2].setIcon(iSand);
					jLSand[i][6].setIcon(iSand);
					jLSand[i][11].setIcon(iSand);
				}

				else if(i == 7 && j ==15 || i == 8 && j ==15){
					// for the above condition set icon in given position
					jLSand[i][1].setIcon(iSand);
					jLSand[i][5].setIcon(iSand);
					jLSand[i][12].setIcon(iSand);
				}
       
				else if(i == 11 && j ==15 || i == 10 && j ==15){
					// for the above condition set icon in given position
					jLSand[i][2].setIcon(iSand);
					jLSand[i][6].setIcon(iSand);
				}
				else if(i == 12 && j == 15){
					// for the above condition set icon in given position
					jLSand[i][0].setIcon(iSandStone);
					for(j = 1;j<16;j++){
						jLSand[12][j].setIcon(iSand);
					}
				}
			}}


		//-----------------------------------------------------------------------------------------------------
		//panelSide and its features
		jPanelSide = new JPanel();				//Creating new Panel					
		jPanelSide.setLayout(null);				//set layout to null		
		jPanelSide.setBounds(600,20, 165, 535); //set xPos, yPos, width and height
		jPanelSide.setBackground(new Color(245,245,0));// background in RGB
		jPanelSide.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //line border in panel
		c.add(jPanelSide);						// adding panel in container		 

		//panelLow and its features
		jPanelLow = new JPanel();				//Creating new Panel							
		jPanelLow.setLayout(null);				//set layout to null							
		jPanelLow.setBounds(6,560,760,55);      //set xPos, yPos, width and height				
		jPanelLow.setBackground(new Color(245,245,245));// background in RGB
		jPanelLow.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //line border in panel
		c.add(jPanelLow);						// adding panel in container						
	

		jLOption = new JLabel("Option:");					
		jLOption.setBounds(20, 13, 50, 20);					
		jLOption.setFont(new Font("Arial", Font.BOLD, 12)); 
		jPanelSide.add(jLOption);							

		jTOption = new JTextField(5);						
		jTOption.setCaretColor(Color.white);	
		jTOption.setBounds(100, 10, 60, 23);
		jPanelSide.add(jTOption);

		jLSquare = new JLabel("Square:");		
		jLSquare.setBounds(20, 36, 50, 20);		
		jLSquare.setFont(new Font("Arial", Font.BOLD, 12));	
		jPanelSide.add(jLSquare);							

		jTSquare = new JTextField(5);						
		jTSquare.setBounds(100, 33, 60, 23);				
		jPanelSide.add(jTSquare);							

		jLDirection = new JLabel("Direction:");				
		jLDirection.setBounds(20, 59, 70, 20);				
		jLDirection.setFont(new Font("Arial", Font.BOLD, 12));
		jPanelSide.add(jLDirection);						

		jTDirection = new JTextField();						
		jTDirection.setBounds(100, 56, 60, 23);				
		jPanelSide.add(jTDirection);						

		jLPoint = new JLabel("Points:");					
		jLPoint.setBounds(20, 83, 60, 20);					
		jLPoint.setFont(new Font("Arial", Font.BOLD, 12));	
		jPanelSide.add(jLPoint);							


		jTPoint = new JTextField();							
		jTPoint.setHorizontalAlignment(JLabel.CENTER);
		jTPoint.setBounds(100, 79, 60, 23);					
		jPanelSide.add(jTPoint);							



		jLInfo = new JLabel();								
		jLInfo.setText("START THE GAME BY PRESSING RUN");
		jLInfo.setBounds(190,510,500,50);					
		//c.add(jLInfo);										

		/*------------------------------------------------*/
		jLTimer = new JLabel("DIGITAL TIMER");				
		jLTimer.setBounds(40,110,90,20);					
		jPanelSide.add(jLTimer);							

		jTHour = new JTextField("00");						
		jTHour.setBackground(Color.black);
		jTHour.setForeground(Color.white);
		jTHour.setHorizontalAlignment(JLabel.CENTER);
		jTHour.setBounds(30,140,25,20);					
		jPanelSide.add(jTHour);								

		jLDot1 = new JLabel(":");							
		jLDot1.setBounds(62,140,50,17);						
		jPanelSide.add(jLDot1);								

		jTMinute = new JTextField("00");					
		jTMinute.setBackground(Color.black);
		jTMinute.setForeground(Color.white);
		jTMinute.setHorizontalAlignment(JLabel.CENTER);
		jTMinute.setBounds(75,140,25,20);					
		jPanelSide.add(jTMinute);							

		jLDot2 = new JLabel(":");							
		jLDot2.setBounds(107,140,50,17);					
		jPanelSide.add(jLDot2);								

		jTSecond = new JTextField("00");					
		jTSecond.setBackground(Color.black);
		jTSecond.setForeground(Color.white);
		jTSecond.setHorizontalAlignment(JLabel.CENTER);
		jTSecond.setBounds(122,140,25,20);					
		jPanelSide.add(jTSecond);


		dWatch = new Timer(1000, new ActionListener() 
		{

			public void actionPerformed(ActionEvent arg0) 
			{
				int hr = count / 60 / 60;
				jTHour.setText(Integer.toString(hr));
				int min = count / 60;
				jTMinute.setText(Integer.toString(min));
				int sec = count % 60;
				jTSecond.setText(Integer.toString(sec));
				count = count + 1;
			}
		});
		/*------------------------------*/

		jBUp = new JButton("^");
		jBUp.setBounds(65,200,45,24);
		jBUp.setBackground(Color.WHITE);
		jBUp.setEnabled(false);
		jPanelSide.add(jBUp);
		jBUp.addActionListener(this);

		jBRight = new JButton(">");
		jBRight.setBounds(110,224,45,24);
		jBRight.setBackground(Color.WHITE);
		jBRight.setEnabled(false);
		jPanelSide.add(jBRight);
		jBRight.addActionListener(this);


		jBLeft = new JButton("<");
		jBLeft.setBounds(20,224,45,24);
		jBLeft.setBackground(Color.WHITE);
		jBLeft.setEnabled(false);
		jPanelSide.add(jBLeft);
		jBLeft.addActionListener(this);


		jBDown = new JButton("v");
		jBDown.setBounds(65,248,45,24);
		jBDown.setBackground(Color.WHITE);
		jBDown.setEnabled(false);
		jPanelSide.add(jBDown);
		jBDown.addActionListener(this);

		jBBlankOne = new JButton();
		jBBlankOne.setBounds(20,200,45,24);
		jBBlankOne.setEnabled(false);
		jPanelSide.add(jBBlankOne);

		jBBlankTwo = new JButton();
		jBBlankTwo.setBounds(110,200,45,24);
		jBBlankTwo.setEnabled(false);
		jPanelSide.add(jBBlankTwo);

		jBBlankThree = new JButton();
		jBBlankThree.setBounds(65,224,45,24);
		jBBlankThree.setEnabled(false);
		jPanelSide.add(jBBlankThree);

		jBBlankFour = new JButton();
		jBBlankFour.setBounds(20,248,45,24);
		jBBlankFour.setEnabled(false);
		jPanelSide.add(jBBlankFour);

		jBBlankFive = new JButton();
		jBBlankFive.setBounds(110,248,45,24);
		jBBlankFive.setEnabled(false);
		jPanelSide.add(jBBlankFive);

		/*-------------------------------------------*/
		jBOptOne = new JButton("Option1");
		jBOptOne.setBounds(4,338,78,24);
		jBOptOne.setBackground(Color.WHITE);
		jBOptOne.setEnabled(false);
		jPanelSide.add(jBOptOne);
		jBOptOne.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent arg0) 
			{
				jTOption.setText("1");
				jBUp.setEnabled(true);
				jBDown.setEnabled(true);
				jBLeft.setEnabled(true);
				jBRight.setEnabled(true);
				jBOptTwo.setEnabled(false);
				jBOptThree.setEnabled(false);				
			}
		}); 

		jBOptTwo = new JButton("Option2");
		jBOptTwo.setBounds(82,338,78,24);
		jBOptTwo.setBackground(Color.WHITE);
		jPanelSide.add(jBOptTwo);
		jBOptTwo.setEnabled(false);

		jBOptTwo.addActionListener(this);
		jBOptTwo.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) 
			{
			}

			public void keyReleased(KeyEvent e) 
			{				
			}

			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == 37){
					System.out.println("Left Pressed");
					moveLeft();
				}

				else if(e.getKeyCode() == 39){
					moveRight();
				}
			}
		});



		jBOptThree = new JButton("ENEMY");
		jBOptThree.setBounds(4,368,78,24);
		jBOptThree.setEnabled(false);
		jBOptThree.setBackground(Color.WHITE);
		jPanelSide.add(jBOptThree);
		jBOptThree.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				rNumber = new Random();
				int  n = rNumber.nextInt(7) + 0;    
				int  n1 = rNumber.nextInt(5) + 1;    
				int  n2 = rNumber.nextInt(8) + 7;    
				int  n3 = rNumber.nextInt(7) + 8;    
				int  n4 = rNumber.nextInt(12) + 3;    

				jLSand[0][n].setIcon(iBallE);
				jLSand[3][n1].setIcon(iBallE);
				jLSand[6][n2].setIcon(iBallE);
				jLSand[3][n2].setIcon(iCoin);
				jLSand[12][n1].setIcon(iCoin);
				jLSand[6][n4].setIcon(iCoin);
				jLSand[0][n3].setIcon(iCoin);
				jLSand[9][n3].setIcon(iBallE);
				jLSand[12][n4].setIcon(iBallE);
				jBOptThree.setEnabled(false);

			}
		});

		jBExit = new JButton();
		jBExit.setText("Exit");
		jBExit.setBounds(82,368,78,24);
		jBExit.setBackground(Color.WHITE);
		jPanelSide.add(jBExit);
		jBExit.addActionListener(this);


		/*---------------------------------------------*/

		iCompass = new ImageIcon("images/east.jpg");
		jBCompass = new JButton(iCompass);
		jBCompass.setBounds(35,418,100,100);
		jBCompass.setBackground(Color.WHITE);
		jPanelSide.add(jBCompass);

		/*---------------------------------------------*/
		iconAct = new ImageIcon("images/step.png");
		jBAct = new JButton("Act",iconAct);
		jBAct.setBounds(130,10,100,30);
		jBAct.setBackground(Color.WHITE);
		jPanelLow.add(jBAct);
		jBAct.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent e) 
			{
				moveLeft();
				fall.start();

			}
		});

		iconRun = new ImageIcon("images/run.png");
		jBRun = new JButton("Run",iconRun);
		jBRun.setBounds(240,10,100,30);
		jBRun.setBackground(Color.WHITE);
		jPanelLow.add(jBRun);
		jBRun.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent arg0) 
			{
				jLInfo.setText("ENJOY THE GAME :)");
				dWatch.start();     		
				jBOptOne.setEnabled(true);
				jBOptTwo.setEnabled(true);
				jBOptThree.setEnabled(true);
				jBRun.setEnabled(false);
			}
		});


		iconReset = new ImageIcon("images/reset.png");
		jBReset = new JButton("Reset",iconReset);
		jBReset.setBounds(350,10,100,30);
		jBReset.setBackground(Color.WHITE);
		jPanelLow.add(jBReset);
		jBReset.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent arg0) 
			{
				reset();
			}
		});

		/*-------------------------------*/
		jLSpeed = new JLabel("Speed :");
		jLSpeed.setBounds(500,15,60,20);
		jPanelLow.add(jLSpeed);

		jSSpeed  = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		jSSpeed.setMajorTickSpacing(25);
		jSSpeed.setPaintTicks(true);
		jSSpeed.setBounds(560,20,180,20);
		jPanelLow.add(jSSpeed);




	}//ends createGUI method



	public void moveLeft(){ 	/* A method to roll ball left */

		if((bCol-1) >= 0 && jLSand[bRow][bCol - 1].getIcon().equals(iSand))
		{
			jLSand[bRow][bCol - 1].setIcon(iBall);
			jLSand[bRow][bCol].setIcon(iSand);
			bCol = bCol - 1;	 
			playRollSound();
			jBCompass.setIcon(iWest);
			jTDirection.setText("WEST");
			jTSquare.setText(bRow +" : "+ bCol);
			System.out.println("Left");
		}
		else if ((bCol-1) >= 0 && jLSand[bRow][bCol - 1].getIcon().equals(iCoin))
		{
			jLSand[bRow][bCol - 1].setIcon(iBall);
			jLSand[bRow][bCol].setIcon(iSand);
			bCol = bCol - 1;	 
			pointL+= 1;
			playDetectSound();
			jTPoint.setText(String.valueOf(pointL));
			jBCompass.setIcon(iWest);
			jTDirection.setText("WEST");
			jTSquare.setText(bRow +" : "+ bCol);
			System.out.println("Left");

		}

		else if((bCol-1) >= 0 && jLSand[bRow][bCol - 1].getIcon().equals(iBallE))
		{
			jLSand[bRow][bCol].setIcon(iSand);
			jLInfo.setText("Opps! You Lose.");
			playDetectSound();
			dWatch.stop();
			jBRun.setEnabled(false);
			jTSquare.setText(bRow +" : "+ bCol);
			JOptionPane.showMessageDialog(null,"Try Again !!!");
		}
		else if((bCol-1) >= 0 && jLSand[bRow][bCol - 1].getIcon().equals(iSandStone))
		{
			jLSand[bRow][bCol - 1].setIcon(iGoldBall);
			jLSand[bRow][bCol].setIcon(iSand);
			playGoalSound();
			jBOptOne.setEnabled(false);
			jBOptTwo.setEnabled(false);
			dWatch.stop();
			jLInfo.setText("You completed the game in "+jTHour.getText()+" "+
			"Hour"+" "+jTMinute.getText()+" Minute"+" "+jTSecond.getText()+" Second.");
			jBRun.setEnabled(false);
			jTSquare.setText(bRow +" : "+ bCol);
			JOptionPane.showMessageDialog(null,"You Won!!!");
		}
		else{
			System.out.println("Stop left");	
		}
	}

	public void moveRight(){	/* A method to roll ball right */

		if((bCol+1) <=15 && jLSand[bRow][bCol + 1].getIcon().equals(iSand))
		{
			jLSand[bRow][bCol + 1].setIcon(iBall);
			jLSand[bRow][bCol].setIcon(iSand);
			bCol = bCol + 1;	
			playRollSound();
			dWatch.stop();
			jBCompass.setIcon(iEast);
			jTDirection.setText("EAST");
			jTSquare.setText(bRow +" : "+ bCol);
			System.out.println("Right");
		}	
		else if((bCol+1) <=15 && jLSand[bRow][bCol + 1].getIcon().equals(iCoin))
		{
			jLSand[bRow][bCol + 1].setIcon(iBall);
			jLSand[bRow][bCol].setIcon(iSand);
			bCol = bCol + 1;	
			pointL+=1;
			playDetectSound();
			jTPoint.setText(String.valueOf(pointL));
			jBCompass.setIcon(iEast);
			jTDirection.setText("EAST");
			jTSquare.setText(bRow +" : "+ bCol);
			System.out.println("Right");
		}	
		else if((bCol+1) <= 15 && jLSand[bRow][bCol + 1].getIcon().equals(iBallE))
		{
			jLSand[bRow][bCol].setIcon(iSand);
			jLInfo.setText("Opps! You Lose.");
			jBRun.setEnabled(false);
			playDetectSound();
			dWatch.stop();
			JOptionPane.showMessageDialog(null,"Try Again !!!");
			jTSquare.setText(bRow +" : "+ bCol);
		}
		else{
			System.out.println("Stop right");
		}
	}

	public void moveUp(){		/* A method to roll ball up */
		if((bRow-1)>= 0 && jLSand[bRow - 1][bCol].getIcon().equals(iSand)== true)
		{
			jLSand[bRow - 1][bCol].setIcon(iBall);
			jLSand[bRow][bCol].setIcon(iSand);
			bRow = bRow - 1;
			playRollSound();
			jBCompass.setIcon(iNorth);
			jTDirection.setText("NORTH");
			jTSquare.setText(bRow +" : "+ bCol);
			System.out.println("UP");
		}
		else if((bRow-1)>= 0 && jLSand[bRow - 1][bCol].getIcon().equals(iCoin)== true)
		{
			jLSand[bRow - 1][bCol].setIcon(iBall);
			jLSand[bRow][bCol].setIcon(iSand);
			bRow = bRow - 1;
			pointL+=1;
			playDetectSound();
			jTPoint.setText(String.valueOf(pointL));
			jBCompass.setIcon(iNorth);
			jTDirection.setText("NORTH");
			jTSquare.setText(bRow +" : "+ bCol);
			System.out.println("UP");
		}
		else if((bRow-1) >= 0 && jLSand[bRow-1][bCol].getIcon().equals(iBallE))
		{
			jLSand[bRow][bCol].setIcon(iSand);
			jTSquare.setText(bRow +" : "+ bCol);
			playDetectSound();
			dWatch.stop();
			JOptionPane.showMessageDialog(null,"Try Again !!!");
		}
		else{
			System.out.println("Stop up");
		}


	}

	public void autoMoveDown()	/* A method to roll ball down every 100 microsecond */
	{

		fall = new Timer(100,new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				moveDown();

			}});

	}

	public void moveDown()		/* A method to roll ball down */
	{
		if((bRow+1) <= 12 && jLSand[bRow + 1][bCol].getIcon().equals(iSand)){
			jLSand[bRow + 1][bCol].setIcon(iBall);
			jLSand[bRow][bCol].setIcon(iSand);
			bRow = bRow + 1;
			playRollSound();
			jBCompass.setIcon(iSouth);
			jTDirection.setText("SOUTH");
			jTSquare.setText(bRow +" : "+ bCol);
			System.out.println("Down");

		}
		else if((bRow+1) <= 12 && jLSand[bRow + 1][bCol].getIcon().equals(iCoin)){
			jLSand[bRow + 1][bCol].setIcon(iBall);
			jLSand[bRow][bCol].setIcon(iSand);
			bRow = bRow + 1;
			pointL+= 1; 
			playDetectSound();
			jTPoint.setText(String.valueOf(pointL));
			jBCompass.setIcon(iSouth);
			jTDirection.setText("SOUTH");
			jTSquare.setText(bRow +" : "+ bCol);
			System.out.println("Down");
		}
		else if((bRow+1) <=12 && jLSand[bRow+1][bCol].getIcon().equals(iBallE))
		{
			jLSand[bRow][bCol].setIcon(iSand);
			jTSquare.setText(bRow +" : "+ bCol);
			playDetectSound();
			dWatch.stop();
			JOptionPane.showMessageDialog(null,"Try Again !!!");
		}


	}

	public void reset()
	{
		dispose();
		CBallMaze.main(null);

		System.out.println("RESET");
	}
	
	public static void playRollSound() {
		try {
	          File rollFile = new File("sounds/roll.wav"); //getting the location of sound file
	          AudioInputStream audioputsin = AudioSystem.getAudioInputStream(rollFile);              
	         Clip sound = AudioSystem.getClip();  // getting the audio clip 
	         sound.open(audioputsin);
	         sound.start();
	      } catch (UnsupportedAudioFileException e) {
	          e.printStackTrace(); //helps to diagnose the exceptions
	       } catch (IOException e) {
	          e.printStackTrace(); //helps to diagnose the exceptions
	       } catch (LineUnavailableException e) {
	          e.printStackTrace(); //helps to diagno se the exceptions
	       }
	    }
	public static void playGoalSound() {
		try {
		    File goalFile = new File("sounds/goal.wav"); //getting the location of sound file
	          AudioInputStream audioputsin = AudioSystem.getAudioInputStream(goalFile);              
	         Clip sound = AudioSystem.getClip();  // getting the audio clip 
	         sound.open(audioputsin);
	         sound.start();
	      } catch (UnsupportedAudioFileException e) {
	          e.printStackTrace(); //helps to diagnose the exceptions
	       } catch (IOException e) {
	          e.printStackTrace(); //helps to diagnose the exceptions
	       } catch (LineUnavailableException e) {
	          e.printStackTrace(); //helps to diagnose the exceptions
	       }
	    }
	public static void playDetectSound() {
		try {
		    File goalFile = new File("sounds/tick.wav"); //getting the location of sound file
	          AudioInputStream audioputsin = AudioSystem.getAudioInputStream(goalFile);              
	         Clip sound = AudioSystem.getClip();  // getting the audio clip 
	         sound.open(audioputsin);
	         sound.start();
	      } catch (UnsupportedAudioFileException e) {
	          e.printStackTrace(); //helps to diagnose the exceptions
	       } catch (IOException e) {
	          e.printStackTrace(); //helps to diagnose the exceptions
	       } catch (LineUnavailableException e) {
	          e.printStackTrace(); //helps to diagnose the exceptions
	       }
	    }
	

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == jBLeft){
			moveLeft();

		}
		else if(e.getSource() == jBRight)
		{
			moveRight();
		}

		else if(e.getSource() == jBUp)
		{
			moveUp();
		}

		else if(e.getSource() == jBDown)
		{
			moveDown();
		}

		else if(e.getSource() == jBOptTwo)
		{
			fall.start();
			jTOption.setText("2");
			jBUp.setEnabled(false);
			jBDown.setEnabled(false);
			jBLeft.setEnabled(false);
			jBRight.setEnabled(false);
			jBOptOne.setEnabled(false);
			jBOptThree.setEnabled(false);
		}

		else if(e.getSource() == jBExit)
		{
			System.exit(0);
		}


	}

}//ends main method

// reset and square left
//http://pngimg.com/imgs/objects/coin/ coin images
