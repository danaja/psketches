package segregation;

import java.util.List;
import java.util.Random;

public class Person {

	private int x;
	private int y;
	private int color;
	private double affinity;

	public Person(int x, int y, int color, double affinity) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.affinity = affinity;

	}

	public double decideMove(int neighbourhood, int[] pixels, int spaceX, int spaceY,List<Integer> vacantList) {

		int count = 0;
		int nsize = 0;

		for (int i = x - neighbourhood; i <= x + neighbourhood; i++) {
			for (int j = y - neighbourhood; j <= y + neighbourhood; j++) {
				if ((i >= 0 && i < spaceX) && (j >= 0 && j < spaceY)) {
					if(pixels[j * spaceX + i] != SegregationModelSketch.VACANT_COLOR)
					{
						nsize++;
					}
					if (pixels[j * spaceX + i] == color) {
						count++;
					}

				}
			}
		}

		double simPerc = (count / (nsize * 1.0));
		
		if(affinity > simPerc)
		{
			moveToVacant(pixels,spaceX,spaceY,vacantList);
		}
		
		return simPerc;

	}

	private void moveToVacant(int[] pixels,int spaceX,int spaceY,List<Integer> vacantList) {
		Random gen = new Random();
		int index = vacantList.remove(gen.nextInt(vacantList.size()));
		vacantList.add(y*spaceX+x);
		pixels[y*spaceX+x] = SegregationModelSketch.VACANT_COLOR;
		x = index % spaceX;
		y = (int)Math.floor(index/spaceX);
		pixels[index] = color;
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
