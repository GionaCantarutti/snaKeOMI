import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class KomiSnake extends PApplet {

PImage topKomi;
PImage midKomi;
PImage bottomKomi;
PImage bendedKomi;

PImage MmidKomi;
PImage MbendedKomi;

PImage eatSprite;

PImage menuKomi;

PImage gameOverKomi;

PImage whitebox;

PImage intrigued;

Scene mainScene;

public void setup() {
 
  
 
  background(255);
  
  topKomi = loadImage("Resources/TopKomi.jpg");
  midKomi = loadImage("Resources/MidKomi.jpg");
  MmidKomi = loadImage("Resources/MidKomi.jpg");
  bottomKomi = loadImage("Resources/BottomKomi.png");
  bendedKomi = loadImage("Resources/BendedKomi.jpg");
  MbendedKomi = loadImage("Resources/BendedKomi.jpg");
    
  eatSprite = loadImage("Resources/Tadano.jpg");
  
  menuKomi = loadImage("Resources/DoesHerBest.jpg");
  
  gameOverKomi = loadImage("Resources/DoYourBest.jpg");
  
  whitebox = loadImage("Resources/whitebox.png");
  
  intrigued = loadImage("Resources/Intrigued.png");
  
  mainScene = new MainMenu();
  
}


public void draw() {
  
  mainScene.update();
  mainScene.display();
  
  println(frameRate);
  
}

public void mousePressed() {
  
  mainScene.mouse();
  
}

public void keyPressed() {
 
  mainScene.key();
  
}
static class Direction {
  
  public static enum Direct {
    NORTH,
    EST,
    SOUTH,
    WEST
  }
  
}
class GameOver extends Scene {
  
  int CDTIME = 60;
  int enterFrameCount;
  
  GameOver() {
    
    enterFrameCount = frameCount;  
    
  }
  
  public void display() {
   
    background(255);
    
    pushMatrix();
    translate(0,-300);
    
    fill(255, 0, 0);
    textSize(200);
    textAlign(CENTER);
    text("GAME OVER", 910, 540);
    
    fill(150);
    textSize(40);
    textAlign(CENTER);
    text("Press any key to restart", 910, 640);
    
    popMatrix();
    
    image(gameOverKomi, 1920/2-gameOverKomi.width/2 - 50, 1080-gameOverKomi.height); 
    
  }
  
  public void mouse() {
    
    if (frameCount - enterFrameCount > CDTIME) {
     
      mainScene = new GameScene1();
      
    }
    
  }
  
  public void key() {
    
    if (frameCount - enterFrameCount > CDTIME) {
     
      mainScene = new GameScene1();
      
    }
    
  }
  
}
class GameScene1 extends Scene {
 
  int ACTIVE_FRAME_CD = 20;
  //int TURNING_CD = -1;
  
  int XBOXES = 16;
  int YBOXES = 9;
  
  Direction.Direct NORTH = Direction.Direct.NORTH;
  Direction.Direct EST = Direction.Direct.EST;
  Direction.Direct SOUTH = Direction.Direct.SOUTH;
  Direction.Direct WEST = Direction.Direct.WEST;
  
  Direction.Direct queue;
  
  int[][] mapArray;
  Direction.Direct[][] previousMap;
  Direction.Direct[][] sequentMap;
  int komiLength;
  int headX, headY;
  int tailX, tailY;
  
  int rotationSign;
  
  int rateoX, rateoY;
  
  int eatX, eatY;
  
  Direction.Direct direction; //NORTH, EST, SOUTH, WEST
  Direction.Direct previous;
  
  ArrayList<IntriguedAnimation> anim;
  
  int activeFrame;
  
  GameScene1() {
    
    komiLength = 4;
    
    mapArray = new int[XBOXES][YBOXES];
    previousMap = new Direction.Direct[XBOXES][YBOXES];
    sequentMap = new Direction.Direct[XBOXES][YBOXES];
    headX = XBOXES/2;
    headY = YBOXES/2;
    activeFrame = 0;
    
    rateoX = 1920/mapArray.length;
    rateoY = 1080/mapArray[0].length;
    
    direction = Direction.Direct.NORTH;
    previous = Direction.Direct.SOUTH;
    
    queue = NORTH;
    
    for (int i = komiLength; i >= 0; i--) {
     
      mapArray[headX][headY+i] = komiLength-i;
      previousMap[headX][headY+i] = SOUTH;
      sequentMap[headX][headY+i] = NORTH;
      
    }
      
    eatX = (int)(random(1) * XBOXES);
    eatY = (int)(random(1) * YBOXES);
    
    while (mapArray[eatX][eatY] != 0) {
     
      eatX = (int)(random(1) * XBOXES);
      eatY = (int)(random(1) * YBOXES);
      
    }
    
    rotationSign = 1;
    
    anim = new ArrayList<IntriguedAnimation>();
    
  }
  
  public void update() {
    
    if (activeFrame == 0) {
      activeFrame = ACTIVE_FRAME_CD; 
      
      //  UPDATES THE GAME   /////////////////////////////////////////////////////////
      
      tickDownMapArray();
      
      move();
      
      checkCollision();
      
      checkEat();
      
      mapArray[headX][headY] = komiLength;
      
      drawKomi();
          
      ////////////////////////////////////////////////////////////////////////////////
      
    } else {
      //TICKS DOWN TILL NEXT UPDATE
      activeFrame--;
    }
    
      //  NORMAL FRAMERATE   /////////////////////////////////////////////////////////
      
      direction = queue;
      
      drawEat();
      
      smoothKomi();
      
      ArrayList<IntriguedAnimation> deletionList = new ArrayList<IntriguedAnimation>();
      
      for(IntriguedAnimation a: anim) {
       
        a.update();
        a.display();
        if (a.dead) {
          deletionList.add(a);
        }
        
      }
      
      for(IntriguedAnimation a: deletionList) {
       
        anim.remove(a);
        a = null;
        
      }
      
      println(anim.size() + "   " + deletionList.size());
      
      
    
  }
  
  public void key() {
    
    Direction.Direct d = queue;
    
    if(key == 'w') {
     
      if(previous != Direction.Direct.NORTH) {
        d = Direction.Direct.NORTH;
      }
      
    } else if(key == 'd') {
     
      if(previous != Direction.Direct.EST) {
        d = Direction.Direct.EST;
      }
      
    } else if(key == 's') {
     
      if(previous != Direction.Direct.SOUTH) {
        d = Direction.Direct.SOUTH;
      }
      
    } else if(key == 'a') {
     
      if(previous != Direction.Direct.WEST) {
        d = Direction.Direct.WEST;
      }
      
    }
    
    queue = d;
    
    //if(activeFrame < ACTIVE_FRAME_CD - TURNING_CD) {
    // activeFrame = 1; 
    //}
    
  }
  
  public void smoothKomi() {
   
    Direction.Direct prev = previousMap[headX][headY];
    Direction.Direct seq = direction;
    
    float deltaPositionX = ((ACTIVE_FRAME_CD - activeFrame)/(float)ACTIVE_FRAME_CD)*rateoX;
    float deltaPositionY = ((ACTIVE_FRAME_CD - activeFrame)/(float)ACTIVE_FRAME_CD)*rateoY;
    
      pushMatrix();
      
          translate(headX*rateoX + rateoX/2,headY*rateoY + rateoY/2);
          
          // BENDED PIECE CHECK
          if ( (prev == seq) || (prev == NORTH && seq == SOUTH) || (prev == SOUTH && seq == NORTH) || (prev == EST && seq == WEST) || (prev == WEST && seq == EST)) {
              if ( prev == Direction.Direct.EST || prev == Direction.Direct.WEST) {
                rotate(PI/2);
              }
              image(MmidKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
          } else {
            if ( (prev == SOUTH && seq == WEST) || (prev == WEST && seq == SOUTH) ) {
              rotate(0);
              image(MbendedKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
            } else if ( (prev == WEST && seq == NORTH) || (prev == NORTH && seq == WEST) ) {
              rotate(PI/2);
              image(MbendedKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
            } else if ( (prev == NORTH && seq == EST) || (prev == EST && seq == NORTH) ) {
              rotate(PI);
              image(MbendedKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
            } else if ( (prev == SOUTH && seq == EST) || (prev == EST && seq == SOUTH) ) {
              rotate(-PI/2);
              image(MbendedKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
            } else {
              text("?", -rateoX/2, -rateoY/2);
            }
          }
              
      popMatrix();
      
      pushMatrix();
      translate((headX*(float)rateoX) + (rateoX/2),(headY*(float)rateoY) + (rateoY/2));
      switch(direction){
        
        case NORTH:
          rotate(0);
        break;
        
        case EST:
          rotate(PI/2);
        break;
        
        case SOUTH:
          rotate(PI);
        break;
        
        case WEST:
          rotate(-PI/2);
        break;
        
        
      }
      
      image(whitebox, -rateoX/2, -rateoY/2 - deltaPositionY, rateoX, rateoY);
      image(topKomi, -rateoX/2, -rateoY/2 - deltaPositionY, rateoX, rateoY);
      popMatrix();
      
      pushMatrix();
      translate((tailX*(float)rateoX) + (rateoX/2),(tailY*(float)rateoY) + (rateoY/2));
      switch(sequentMap[tailX][tailY]){
        
        case NORTH:
          rotate(0);
        break;
        
        case EST:
          rotate(PI/2);
        break;
        
        case SOUTH:
          rotate(PI);
        break;
        
        case WEST:
          rotate(-PI/2);
        break;
        
        
      }
      
      image(bottomKomi, -rateoX/2, -rateoY/2 - deltaPositionY, rateoX, rateoY);
      popMatrix();
      
    
  }
  
  public void drawEat() {
   
    pushMatrix();
    translate((eatX*rateoX) + rateoX/2,(eatY*rateoY) + rateoY/2);
    rotate(sin(frameCount/35.0f)/3);
    image(eatSprite, -rateoX/2, -rateoY/2, rateoX, rateoY);
    popMatrix();
    
    rotationSign *= -1;
    
  }
  
  public void checkEat() {
   
    if (headX == eatX && headY == eatY) {
     
      eaten();
      
    }
    
  }
  
  public void eaten() {
    
    //anim.add(new IntriguedAnimation());
   
    eatX = (int)(random(1) * XBOXES);
    eatY = (int)(random(1) * YBOXES);
    
    while (mapArray[eatX][eatY] != 0) {
     
      eatX = (int)(random(1) * XBOXES);
      eatY = (int)(random(1) * YBOXES);
      
    }
    
    komiLength++;
    
  }
  
  public void drawKomi() {
    
    background(255);
    
    for (int i = 0; i < mapArray.length; i++) {
      for (int j = 0; j < mapArray[0].length; j++) {
        
        int status = mapArray[i][j];
        
        Direction.Direct prev = previousMap[i][j];
        Direction.Direct seq = sequentMap[i][j];
        
        if(status != 0) {
        
        pushMatrix();
        translate(i*rateoX, j*rateoY);
        
        if (status == komiLength) {
          
          
          pushMatrix();
          translate(rateoX/2,rateoY/2);
          
          switch (prev) {
            case NORTH:
              rotate(PI);
              break;
            case EST:
              rotate(-PI/2);
              break;
            case SOUTH:
              rotate(0);
              break;
            case WEST:
              rotate(PI/2);
              break;
          }
          
          //image(topKomi, -rateoX/2,-rateoY/2, rateoX, rateoY); 
          popMatrix();
          
          
        } else if (status > 1) {
          
          
          pushMatrix();
          translate(rateoX/2,rateoY/2);
          
          // BENDED PIECE CHECK
          if ( (prev == seq) || (prev == NORTH && seq == SOUTH) || (prev == SOUTH && seq == NORTH) || (prev == EST && seq == WEST) || (prev == WEST && seq == EST)) {
              if ( prev == Direction.Direct.EST || prev == Direction.Direct.WEST) {
                rotate(PI/2);
              }
              image(midKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
          } else {
            if ( (prev == SOUTH && seq == WEST) || (prev == WEST && seq == SOUTH) ) {
              rotate(0);
              image(bendedKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
            } else if ( (prev == WEST && seq == NORTH) || (prev == NORTH && seq == WEST) ) {
              rotate(PI/2);
              image(bendedKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
            } else if ( (prev == NORTH && seq == EST) || (prev == EST && seq == NORTH) ) {
              rotate(PI);
              image(bendedKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
            } else if ( (prev == SOUTH && seq == EST) || (prev == EST && seq == SOUTH) ) {
              rotate(-PI/2);
              image(bendedKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
            } else {
              text("?", -rateoX/2, -rateoY/2);
            }
          }
              
          popMatrix();
          
          
        } else if (status == 1) {
          
          tailX = i; tailY = j;
          
          pushMatrix();
          translate(rateoX/2,rateoY/2);
          
          switch (seq) {
            case NORTH:
              rotate(0);
              break;
            case EST:
              rotate(PI/2);
              break;
            case SOUTH:
              rotate(PI);
              break;
            case WEST:
              rotate(-PI/2);
              break;
          }
          
          //image(bottomKomi, -rateoX/2,-rateoY/2, rateoX, rateoY);
          
          popMatrix();
          
          
        }
        
        popMatrix();
        
        }
        
      }
    }
    
  }
  
  public void move() {
      
      try {
      
        switch(direction) {
        case NORTH:
          sequentMap[headX][headY] = Direction.Direct.NORTH;
          headY--;
          previous = Direction.Direct.SOUTH;
          previousMap[headX][headY] = Direction.Direct.SOUTH;
          break;
        case EST:
          sequentMap[headX][headY] = Direction.Direct.EST;
          headX++;
          previous = Direction.Direct.WEST;
          previousMap[headX][headY] = Direction.Direct.WEST;
          break;
        case SOUTH:
          sequentMap[headX][headY] = Direction.Direct.SOUTH;
          headY++;
          previous = Direction.Direct.NORTH;
          previousMap[headX][headY] = Direction.Direct.NORTH;
          break;
        case WEST:
          sequentMap[headX][headY] = Direction.Direct.WEST;
          headX--;
          previous = Direction.Direct.EST;
          previousMap[headX][headY] = Direction.Direct.EST;
          break;
      }
        
    } catch (ArrayIndexOutOfBoundsException e) {
      
        switch(direction) {
        case NORTH:
          sequentMap[headX][headY += 2] = Direction.Direct.NORTH;
          headY = mapArray[0].length-1;
          previous = Direction.Direct.SOUTH;
          previousMap[headX][headY] = Direction.Direct.SOUTH;
          break;
        case EST:
          sequentMap[headX -= 2][headY] = Direction.Direct.EST;
          headX = 0;
          previous = Direction.Direct.WEST;
          previousMap[headX][headY] = Direction.Direct.WEST;
          break;
        case SOUTH:
          sequentMap[headX][headY -= 2] = Direction.Direct.SOUTH;
          headY = 0;
          previous = Direction.Direct.NORTH;
          previousMap[headX][headY] = Direction.Direct.NORTH;
          break;
        case WEST:
          sequentMap[headX += 2][headY] = Direction.Direct.WEST;
          headX = mapArray.length-1;
          previous = Direction.Direct.EST;
          previousMap[headX][headY] = Direction.Direct.EST;
          break;
      }
    }
    
  }
  
  public void tickDownMapArray() {
    
    for(int i = 0; i < mapArray.length; i++) {
        for(int j = 0; j < mapArray[i].length; j++) {
          mapArray[i][j] = Math.max(mapArray[i][j] - 1, 0);
        }
      }
      
  }
  
  public void checkCollision() {
    
    try {
      if (mapArray[headX][headY] > 0) {
        
        mainScene = new GameOver();
        
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      
        switch(direction) {
        case NORTH:
          headY = mapArray[0].length-1;
          previous = Direction.Direct.SOUTH;
          previousMap[headX][headY] = Direction.Direct.SOUTH;
          break;
        case EST:
          headX = 0;
          previous = Direction.Direct.WEST;
          previousMap[headX][headY] = Direction.Direct.WEST;
          break;
        case SOUTH:
          headY = 0;
          previous = Direction.Direct.NORTH;
          previousMap[headX][headY] = Direction.Direct.NORTH;
          break;
        case WEST:
          headX = mapArray.length-1;
          previous = Direction.Direct.EST;
          previousMap[headX][headY] = Direction.Direct.EST;
          break;
      }
      
      checkCollision();
      
      
    }
      
  }
  
}
class IntriguedAnimation {
  
  float elapsedFrames;
  float deathFrame;
  float alpha;
  public boolean dead;
  
  float w, h;
 
  IntriguedAnimation() {
    
    w = 400;
    h = 225;
    
    deathFrame = 120;
    
    
    elapsedFrames = 0;
    alpha = 0;
    dead = false;
    
  }
  
  public void update() {
    
    elapsedFrames++;
    if (elapsedFrames == deathFrame) {dead = true;}
    alpha = Math.max((255 - ((elapsedFrames/(float)deathFrame) * 255) - 50), 0);
    
  }
  
  public void display() {
    
    fill(255, alpha);
    rect(1920-w-elapsedFrames, 1000-h, w, h);
    noStroke();
    tint(255, alpha);
    image(intrigued, 1920-w-elapsedFrames, 1000-h, w, h);
    tint(255, 255);
    
  }
  
}
class MainMenu extends Scene {
  
  MainMenu() {
    
  }
  
  public void display() {
    
    background(255);
    
    image(menuKomi, width-menuKomi.width, height-menuKomi.height);
    
    pushMatrix();
    translate(-30, 0);
    
    textAlign(CENTER);
    textSize(250);
    fill(240, 240, 0);
    text("Sna", 1920/2 - 300, 200);
    fill(0, 255, 0);
    text("K", 1920/2, 200);
    fill(0, 0, 255);
    text("omi", 1920/2 + 350,200);
    fill(0, 255, 0);
    textSize(100);
    text("e", 1920/2 + 90, 200);

    popMatrix();
    
    textSize(50);
    fill(0);
    
    pushMatrix();
    translate(1920/2, 400);
    rotate(sin(frameCount/35.0f)/3);
    
    text("Press any button to start", 0,0);
    
    popMatrix();
    
  }
  
  public void key() {
   
    mainScene = new GameScene1();
    
  }
  
  public void mouse() {
    
    mainScene = new GameScene1();
    
  }
   
}
  
class Scene {
 
  
  Scene() {
    
  }
  
  public void display() {
    
  }
  
  public void key() {
    
  }
  
  public void mouse() {
    
  }
  
  public void update() {
    
  }
  
  
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "KomiSnake" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
