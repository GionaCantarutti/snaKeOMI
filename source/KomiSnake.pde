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

void setup() {
 
  fullScreen();
 
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


void draw() {
  
  mainScene.update();
  mainScene.display();
  
  println(frameRate);
  
}

void mousePressed() {
  
  mainScene.mouse();
  
}

void keyPressed() {
 
  mainScene.key();
  
}
