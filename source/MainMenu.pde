class MainMenu extends Scene {
  
  MainMenu() {
    
  }
  
  void display() {
    
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
    rotate(sin(frameCount/35.0)/3);
    
    text("Press any button to start", 0,0);
    
    popMatrix();
    
  }
  
  void key() {
   
    mainScene = new GameScene1();
    
  }
  
  void mouse() {
    
    mainScene = new GameScene1();
    
  }
   
}
  
