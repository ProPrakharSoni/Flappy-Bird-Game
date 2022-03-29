package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,bottomTube,topTube;
	//ShapeRenderer shapeRenderer;// just like batch
    Texture gameover,gamestart;

	Circle birdCircle;
	int score=0;
	int scoringTube=0;
	BitmapFont font;
    boolean go = true;
	int maxHeight;
	int flapState=0;
	Texture[] birds;
	float birdY=0;
	float velocity=0;
	int gameState =0;
	float gravity=2;
	float gap =490;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity =4;
	int numberOfTubes=4;
	float[] tubeX= new float[numberOfTubes];
	float[] tubeOffset= new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		background = new Texture("bg1.jpeg");
		bottomTube = new Texture("bottomtube.png");
		topTube    = new Texture("toptube.png");
		gamestart = new Texture("cheem_start.png");
        birds=new Texture[2];
        maxHeight=Gdx.graphics.getHeight();
        gameover=new Texture("game_over.png");
        birds[0]=new Texture("cheem.png");
		birds[1]=new Texture("cheem2.png");
		maxTubeOffset=Gdx.graphics.getHeight()/2-gap/2-100;
		randomGenerator=new Random();
		//tubeX=Gdx.graphics.getWidth()/2-topTube.getWidth()/2;
		distanceBetweenTubes=Gdx.graphics.getWidth()*0.7f;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles=new Rectangle[numberOfTubes];
		startGame();
	}

	public void startGame(){
		birdY=Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;
		for(int i=0;i<numberOfTubes;i++){
			tubeOffset[i]=(randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
			tubeX[i]=Gdx.graphics.getWidth()/2-topTube.getWidth()/2+Gdx.graphics.getWidth()+i*distanceBetweenTubes;

			topTubeRectangles[i]=new Rectangle();
			bottomTubeRectangles[i]=new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//if(go) {
		//	batch.draw(gamestart, Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 8, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
		//	go =false;
		//}
		if(gameState == 1) {

			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
                Gdx.app.log("Score",Integer.toString(score));
				if(scoringTube<numberOfTubes-1){
					scoringTube++;
				}else{
					scoringTube=0;
				}
			}

//			batch.draw(topTube,Gdx.graphics.getWidth()/2-topTube.getWidth()/2,Gdx.graphics.getHeight()/2+gap/2-maxTubeOffset,topTube.getWidth(),Gdx.graphics.getHeight());
//			batch.draw(bottomTube,Gdx.graphics.getWidth()/2-bottomTube.getWidth()/2,Gdx.graphics.getHeight()/2-Gdx.graphics.getHeight()-gap/2-maxTubeOffset,bottomTube.getWidth(),Gdx.graphics.getHeight());

			if(Gdx.input.justTouched()){
				velocity=-30;
			}
			for(int i=0;i<numberOfTubes;i++) {
				if(tubeX[i]<-topTube.getWidth()){

					tubeX[i]+=numberOfTubes*distanceBetweenTubes;
					tubeOffset[i]=(randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);

				}else{
					tubeX[i]=tubeX[i]-tubeVelocity;

				}
				tubeX[i] = tubeX[i] - tubeVelocity;
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), Gdx.graphics.getHeight());
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() - gap / 2 + tubeOffset[i], bottomTube.getWidth(), Gdx.graphics.getHeight());
				batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		        topTubeRectangles[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(), Gdx.graphics.getHeight());
		        bottomTubeRectangles[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() - gap / 2 + tubeOffset[i], bottomTube.getWidth(), Gdx.graphics.getHeight());
			}
			if(birdY>0){
				velocity = velocity + gravity;
				if(birdY-birds[flapState].getHeight()<maxHeight) {
					birdY -= velocity;
				}else if(velocity>0){
					birdY -= velocity;
				}
			}
			else{
				gameState=2;
			}

		}else if(gameState == 0){
			batch.draw(gamestart, Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if(gameState == 2){
			batch.draw(gameover,Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/2-Gdx.graphics.getHeight()/4,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
			if(Gdx.input.justTouched()){

				startGame();
				gameState=1;
				score=0;
				scoringTube=0;
				velocity=0;

			}
		}
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}


		font.draw(batch,Integer.toString(score),100,200);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	//	shapeRenderer.setColor(Color.RED);
	  //  shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for(int i=0;i<numberOfTubes;i++) {
		//	shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(), Gdx.graphics.getHeight());
		 //   shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() - gap / 2 + tubeOffset[i], bottomTube.getWidth(), Gdx.graphics.getHeight());
		    if(Intersector.overlaps(birdCircle,topTubeRectangles[i])||Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){
				gameState=2;
			}

		}
	  //  shapeRenderer.end();
	}

}
