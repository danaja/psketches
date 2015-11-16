package segregation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import processing.core.*;

public class SegregationModelSketch extends PApplet {

	List<Person> people = new ArrayList<Person>();
	double loading = 0.8;
	int spaceX = 600;
	int spaceY = 600;
	int numberOfGroups = 2;
	double[] popDist = { 0.5,0.5 };
	double affinity = 0.5;
	List<Integer> vacantList = new ArrayList<Integer>();
	int neighbourhood = 5;
	public static int VACANT_COLOR;

	public static void main(String[] args) {
		String[] a = { "MAIN" };
		PApplet.runSketch(a, new SegregationModelSketch());
	}

	public void settings() {
		size(spaceX, spaceY);
		
	}

	public void setup() {
		frameRate(1);
		VACANT_COLOR = color(0,0,0);
		background(0,0,0);
		loadPixels();
		populateVacant();
		generatePopulation(pixels);
		updatePixels();
		
	}

	public void draw() {
		
		loadPixels();
		System.out.println(updatePopulation(pixels));
		updatePixels();
		
		
	}
	
	private void populateVacant()
	{
		for(int i=0;i<spaceX;i++)
		{
			for(int j=0;j<spaceY;j++){
				vacantList.add(j*spaceX+i);
			}
		}
	}

	private void generatePopulation(int[] pixels){
		int numberOfPeople = (int) Math.round(loading * spaceX * spaceY);
		Person person;
		
		int[] populations = new int[numberOfGroups];
		for (int i = 0; i < numberOfGroups; i++) {
			populations[i] = (int) Math.round(popDist[i] * numberOfPeople);
		}
		int x;
		int y;
		int group;
		boolean vacant = false;
		Random gen = new Random();
		int[] colors = new int[numberOfGroups];
		
		for(int i =0;i<colors.length;i++){
			colors[i] = color(gen.nextInt(255),gen.nextInt(255),gen.nextInt(255));
		}
		
		
		List<Integer> selections = new ArrayList<Integer>();
		
		int index = 0;
		for(int j=0;j<numberOfGroups;j++)
		{
			for(int k = index;k<populations[j];k++){
				selections.add(j);
			}
		}	
				
		
		for (int i = 0; i < numberOfPeople; i++) {			
					vacant = false;
					do {
						x = gen.nextInt(spaceX);
						y = gen.nextInt(spaceY);
						if (pixels[y*spaceX+x] == color(0,0,0)) {
							vacant = true;	
							vacantList.remove(new Integer(y*spaceX+x));
						}
					} while (!vacant);
					
					group = colors[getGroup(selections, gen)];
					pixels[y*spaceX+x] = group;
					person = new Person(x, y,group,affinity);
					people.add(person);
			
		}
	}

	private int getGroup(List<Integer> selections,Random gen){
		int remaining = selections.size();
		if(remaining > 0){
		 int index = gen.nextInt(remaining);
		 return selections.remove(index);
		}
		return -1;
	}
		
	

	private double updatePopulation(int []pixels){	
		double simPerc = 0;
		for(Person person : people){
			simPerc+=person.decideMove(neighbourhood, pixels, spaceX, spaceY,vacantList);
		}
		return (simPerc/people.size());
	}
	
	
	
	
}
