import java.applet.*;
import java.awt.*;
import java.util.*;

public class Snake extends GameObject {	
	public static final int RIGHT = 0;
	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int DOWN = 3;
	public static final int NO_DIR = -1;
	public static final int NUM_DIRS	= 4;
	public static final int DEFAULT_MAX_LENGTH = 50;
	
	private int scoreLossPeriod = 250;
	private int scoreLossAmount;
	private int buffer = 1;
	private boolean growFixedAmount;
	private int growthAmountPerMeal;
	private int fadeLength;
	private int startLength;

	private int direction;
	private int lives = 1;
	private int maxLength = DEFAULT_MAX_LENGTH;
	private Paint unfadedPaint;
	private AudioClip diesClip;
	private AudioClip eatsClip;	
			
	private int score = 0;
	private int scoreLossIndex = 0;
	private int fadingToDo;
	private int lastSimulatedDirection;
	private int waitingCycles = 0;	
	protected int[] bodyPartXs;
	protected int[] bodyPartYs;	
	protected int head;	
	private int tail;		
	private int length;
	private int growingToDo = 0;
	private boolean isAlive = true;	
	private boolean shouldClear = false;
	
	public Snake() {
		lastSimulatedDirection = NO_DIR;
		setMaxLength(DEFAULT_MAX_LENGTH);
	}
		
	public void setScoreLossPeriod(int val) {scoreLossPeriod = val;}
	public void setScoreLossAmount(int val) {scoreLossAmount = val;}
	public void setMaxLength(int maxLength) {
		bodyPartXs = new int[maxLength];
		bodyPartYs = new int[maxLength];
		this.maxLength = maxLength;
	}
	public void setBuffer(int val) {buffer = val;}
	public void setGrowFixedAmount(boolean val) {growFixedAmount = val;}
	public void setGrowthAmountPerMeal(int val) {growthAmountPerMeal = val;}
	public void setFadeDuration(int val) {fadeLength = val;}
	public void setStartLength(int val) {startLength = val;}	
	public void setLives(int val) {lives = val;}
	public void setUnfadedPaint(Paint val) {unfadedPaint = val;}
	public void setDiesSound(AudioClip val) {diesClip = val;}
	public void setEatsSound(AudioClip val) {eatsClip = val;}
	public void setScore(int score) {
		this.score = score;
		setChanged();
		notifyObservers();
	}
	public synchronized void setDirection(int direction) {
		if (!isInverse(lastSimulatedDirection, direction)) {
			this.direction = direction;
		}
	}
	
	public int getScoreLossPeriod() {return scoreLossPeriod;}
	public int getScoreLossAmount() {return scoreLossAmount;}
	public int getBuffer() {return buffer;}
	public boolean getGrowFixedAmount() {return growFixedAmount;}
	public int getGrowthAmountPerMeal() {return growthAmountPerMeal;}
	public int getFadeDuration() {return fadeLength;}
	public int getStartLength() {return startLength;}
	public int getLives() {return lives;}
	public int getScore() {return score;}
	public int getDirection() {return direction;}	
	public int getHeadX() {return bodyPartXs[head];}
	public int getHeadY() {return bodyPartYs[head];}
	public Paint getUnfadedPaint() {return unfadedPaint;}
	public AudioClip getEatsSound() {return eatsClip;}
	public AudioClip getDiesSound() {return diesClip;}
	
	public boolean getIsAlive() {return isAlive;}
		
	public void init(int length, int tailX, int tailY, int direction) {
		setPaint(unfadedPaint);
		isAlive = true;
		this.direction = direction;	
		this.length = 1;
		head = 0;
		tail = head;
		bodyPartXs[head] = tailX;
		bodyPartYs[head] = tailY;
		growingToDo = length - 1;
		for (int i = 0; i < length; i++) {						
			simulate();
		}
	}
	public void randomInit() {
		int x, y;
		int tailX, tailY;
		int dir = 0;
		this.length = startLength;
		// Go until a successful initialization; one in which the snake does
		// not die on creation.		
		boolean obstructed;
		do {
			obstructed = false;
			x = (int) (Math.random() * (double) (getParent().getFieldWidth() - 1));
			y = (int) (Math.random() * (double) (getParent().getFieldHeight() - 1));			
			tailX = x;
			tailY = y;
			dir = (int) (Math.random() * 4.0);
			
			for (int i = 0; i < length + buffer; i++) {
				if (getParent().getObjectAt(x, y) != null) {
					obstructed = true;
					break;
				}
				x += xComp(dir);
				y += yComp(dir);
			}
		} while(obstructed);
		init(startLength, tailX, tailY, dir);
	}		
	public void simulate() {
		lastSimulatedDirection = getDirection();
		if (waitingCycles > 0) {
			waitingCycles--;
		} else if (isAlive) {			
			scoreLossIndex = (scoreLossIndex + 1) % scoreLossPeriod;
			if (scoreLossIndex == 0) {
				setScore(Math.max(0, getScore() - scoreLossAmount));
			}
			
			if (growingToDo > 0) {
				growingToDo--;		
				grow();				
			} else if (growingToDo < 0) {
				growingToDo++;
				shrink();
			} else {
				trimTail();
				advanceHead();				
			}					
		} else if (fadingToDo > 0) {
			fade();
			fadingToDo--;
			if (fadingToDo == 0) {
				shouldClear = true;
			}
		} else if (shouldClear) {
			shouldClear = false;			
			clear();
		}
	}
	public void clear() {
		// the length - 1 end point is to prevent the wall it bumped from
		// being trimmed
		for (int i = 0; i < length - 1; i++) {
			trimTail();
		}				
		if (lives > 0) {
			isAlive = true;
			randomInit();
			waitingCycles = 5;
		}
	}
	public void fade() {
		if (getPaint() instanceof GradientBean) {
			GradientBean initial = (GradientBean) getPaint();
			setPaint(new GradientBean(initial.getPoint1(),
				fadedColor(initial.getColor1()), initial.getPoint2(), 
				fadedColor(initial.getColor2()), initial.isCyclic()));
		}

		int bodyPart = tail;
		for (int i = 0; i < length - 1; i++) {					
			bodyPart = (bodyPart + 1) % length;
			getParent().clear(bodyPartXs[bodyPart], bodyPartYs[bodyPart]);
		}

		bodyPart = tail;
		for (int i = 0; i < length - 1; i++) {					
			bodyPart = (bodyPart + 1) % length;
			obstruct(bodyPartXs[bodyPart], bodyPartYs[bodyPart]);
		}		
	}
	public void grow() {
		if (length < maxLength) {					
			for (int i = length; i > head; i--) {
				bodyPartXs[i + 1] = bodyPartXs[i];
				bodyPartYs[i + 1] = bodyPartYs[i];
			}
			length++;
			advanceHead();
			tail = head;
		}
	}
	public void shrink() {
		if (length > 1) {					
			trimTail();
			length--;
			for (int i = tail; i < length; i++) {
				bodyPartXs[i] = bodyPartXs[i + 1];
				bodyPartYs[i] = bodyPartYs[i + 1];
			}
			tail = (tail + length - 1) % length;
			head = tail;
		}
	}
	public void die() {
		if (diesClip != null) {
			diesClip.play();
		}
		isAlive = false;
		lives--;		
		fadingToDo = fadeLength;
		setChanged();
		notifyObservers();
	}	
	private void trimTail() {
		if (length > 0) {
			tail = (tail + 1) % length;		
			getParent().clear(bodyPartXs[tail], bodyPartYs[tail]);
		}
	}
	private void advanceHead() {
		if (length > 0) {		
			int oldHead = head;				
			head = (head + 1) % length;		
			int nextX = bodyPartXs[oldHead] + xComp(direction);
			int nextY = bodyPartYs[oldHead] + yComp(direction);
			GameObject obj = getParent().getObjectAt(nextX, nextY);
			if (obj != null && !(obj instanceof Goodie)) {
				die();
			} else {								
				bodyPartXs[head] = nextX;
				bodyPartYs[head] = nextY;
				obstruct(bodyPartXs[head], bodyPartYs[head]);
				
				if (obj instanceof Goodie) {				
					Goodie food = (Goodie) obj;
					int numericValue = food.getNumericValue();
					setScore(getScore() + getParent().getLevel());					
					if (numericValue == 9) {
						getParent().setLevel(getParent().getLevel() + 1);
					} else {
						if (eatsClip != null) {
							eatsClip.play();
						}
						if (growFixedAmount) {
							growingToDo = growthAmountPerMeal;
						} else {
							growingToDo = numericValue;
						}
						food.incrementNumber();
						food.randomInit();
						
						// This is to counter the fact that food.randomInit()
						// clears the food's square.
						obstruct(bodyPartXs[head], bodyPartYs[head]);
					}					
				}				
			}
		}		
	}
	public synchronized void levelAdvanced(int level) {
		if (lives > 0) {
			randomInit();
		}
	}
	public boolean isInverse(int dir1, int dir2) {
		return dir1 != NO_DIR && dir2 != NO_DIR && dir1 == inverse(dir2);
	}
	public int inverse(int dir) {
		switch (dir) {
			case UP: return DOWN;
			case DOWN: return UP;
			case LEFT: return RIGHT;
			case RIGHT: return LEFT;
			default: 
				System.out.println("Bad direction: " + dir);
				return -1;
		}
	}
	public static int yComp(int direction) {
		if (direction == UP) return -1;
		else if (direction == DOWN) return 1;
		else return 0;
	}
	public static int xComp(int direction) {
		if (direction == RIGHT) return 1;
		else if (direction == LEFT) return -1;
		else return 0;
	}
	private Color fadedColor(Color c) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha() - 255 / fadeLength);
	}			
	private void obstruct(int x, int y) {
		getParent().obstruct(x, y, this);
	}	
}