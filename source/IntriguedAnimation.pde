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
  
  void update() {
    
    elapsedFrames++;
    if (elapsedFrames == deathFrame) {dead = true;}
    alpha = Math.max((255 - ((elapsedFrames/(float)deathFrame) * 255) - 50), 0);
    
  }
  
  void display() {
    
    fill(255, alpha);
    rect(1920-w-elapsedFrames, 1000-h, w, h);
    noStroke();
    tint(255, alpha);
    image(intrigued, 1920-w-elapsedFrames, 1000-h, w, h);
    tint(255, 255);
    
  }
  
}
