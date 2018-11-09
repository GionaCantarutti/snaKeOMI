class GameOver extends Scene {
  
  int CDTIME = 60;
  int enterFrameCount;
  
  GameOver() {
    
    enterFrameCount = frameCount;  
    
  }
  
  void display() {
   
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
  
  void mouse() {
    
    if (frameCount - enterFrameCount > CDTIME) {
     
      mainScene = new GameScene1();
      
    }
    
  }
  
  void key() {
    
    if (frameCount - enterFrameCount > CDTIME) {
     
      mainScene = new GameScene1();
      
    }
    
  }
  
}
