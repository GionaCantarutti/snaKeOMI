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
  
  void update() {
    
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
  
  void key() {
    
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
  
  void smoothKomi() {
   
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
  
  void drawEat() {
   
    pushMatrix();
    translate((eatX*rateoX) + rateoX/2,(eatY*rateoY) + rateoY/2);
    rotate(sin(frameCount/35.0)/3);
    image(eatSprite, -rateoX/2, -rateoY/2, rateoX, rateoY);
    popMatrix();
    
    rotationSign *= -1;
    
  }
  
  void checkEat() {
   
    if (headX == eatX && headY == eatY) {
     
      eaten();
      
    }
    
  }
  
  void eaten() {
    
    //anim.add(new IntriguedAnimation());
   
    eatX = (int)(random(1) * XBOXES);
    eatY = (int)(random(1) * YBOXES);
    
    while (mapArray[eatX][eatY] != 0) {
     
      eatX = (int)(random(1) * XBOXES);
      eatY = (int)(random(1) * YBOXES);
      
    }
    
    komiLength++;
    
  }
  
  void drawKomi() {
    
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
  
  void move() {
      
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
  
  void tickDownMapArray() {
    
    for(int i = 0; i < mapArray.length; i++) {
        for(int j = 0; j < mapArray[i].length; j++) {
          mapArray[i][j] = Math.max(mapArray[i][j] - 1, 0);
        }
      }
      
  }
  
  void checkCollision() {
    
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
