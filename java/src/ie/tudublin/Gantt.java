package ie.tudublin; 

import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.*;

public class Gantt extends PApplet
{	
	ArrayList<Task> tasks = new ArrayList<Task>();
	
	public void settings()
	{
		size(800, 600);

		loadTasks();
		printTasks();
		
	}

	public void loadTasks()
	{
		Table table = loadTable("tasks.csv", "header");

		for(TableRow row:table.rows())
		{
			Task task = new Task(row);
			tasks.add(task);
		}
	}

	public void printTasks()
	{
		for(Task t:tasks)
		{
			println(t);
		}
	}

	private boolean isEnd = false;
	private int whichTask = -1;
	private int maxMonths = 30;

	private float border = 40;
	private float rowHeight = 40;
	float namesPart = 150;

	public void mousePressed()
	{
		for(int i = 0 ; i < tasks.size() ; i ++)
		{
			//Setting poistion where the user can drag the graphs
			float y1 = (border + border + rowHeight * i) - 15;
			float y2 = (border + border + rowHeight * i) + 20;

			//getting rectangles starts and eneds
			float x1 = map(tasks.get(i).getStart(), 1, maxMonths, namesPart, width - border);
			float x2 = map(tasks.get(i).getEnd(), 1, maxMonths, namesPart, width - border);
			
			//if the mouse click is in between the starting rectangle mapping and in between the approriate y level
			if (mouseX >= x1 && mouseX <= x1 + 20 && mouseY >= y1 && mouseY <= y2)
			{
				//find which task from for loop he is pressing on
				whichTask = i;
				//since starting then end is false
				isEnd = false;
				return;
			}

			if (mouseX <= x2 && mouseX >= x2 - 20 && mouseY >= y1 && mouseY <= y2)
			{
				whichTask = i;
				isEnd = true;
				return;
			}
		}		
		// default value for whichTask
		whichTask = -1;	
	}

	public void mouseDragged()
	{
		if (whichTask != -1)
		{
			//starting value is at mouseX, intbeween names part and width- border(so out graph), raising value by one until max months
			int month = (int)map(mouseX, namesPart, width - border, 1, maxMonths);

			//if the starting potion is within range of months
			if (month >= 1 && month <= maxMonths)
			{
				//which task from mouse presses
				Task task = tasks.get(whichTask); 
				//checking if the end is grabbed or start
				if (isEnd)
				{
					//grabbign private fieled making sure that the drag doesnt make the task 0
					if (month - task.getStart() > 0)
					{
						//setting a new end
						task.setEnd(month);
					}
				}
				else
				{
					if (task.getEnd() - month > 0 )
					{
						task.setStart(month);
					}
				}
			}
		}
	}

	void displayTasks()
	{
		
		textSize(14);
		// textAlign(CENTER,CENTER);
		
		
		// textAlign(RIGHT, CENTER);
		stroke(128);//Lines colour on chart
		for(int i = 1 ; i <= maxMonths ; i ++)
		{
			//Map
			//map(which position(value) is being mapped , from starting number, to last number, starting position from left, to end potion right)
			float x = map(i, 1, maxMonths, namesPart, width - border);
			//Creating the lines mapped from x-x with border = how much from the top and height-broder how much from the bottom
			line(x, border, x, height - border);
			//fill in text coolour
			fill(255);
			//text is 1-maxmonths, starting from where current x is mapped to, how far it is from the top
			text(i, x, 20);
		}

		//Colours of bars and side names
		// textAlign(LEFT, CENTER);
		for(int i = 0 ; i < tasks.size() ; i ++)
		{
			float y = border + border + rowHeight * i;
			fill(255);
			//text getting private name from tasks changing by y, starting from the border, and y chaning as for loop goes to create different postion from the top
			text(tasks.get(i).getName(), border, y);

			// Print the task
			//No boxes around the graph objects
			noStroke();
			//filling map(positon i, from 0 to task array size, from colours 0 to 255), contrast, britghness
			fill(map(i, 0, tasks.size(), 0, 255), 255, 255);

			//getting the first tasks(i) starting poistion form 1 - maxmonths starting namesPart from the left till width -border from right
			float x1 = map(tasks.get(i).getStart(), 1, maxMonths, namesPart, width - border);
			float x2 = map(tasks.get(i).getEnd(), 1, maxMonths, namesPart, width - border);
			//starting from x1, how much from the top, where it ends, how thick the bar is, how rounded off it is
			rect(x1, y - 20, x2 - x1, rowHeight - 5, 5);
		}

	}

	public void setup() 
	{
		colorMode(HSB);
	}
	
	public void draw()
	{			
		background(0);
		displayTasks();
	}
}
