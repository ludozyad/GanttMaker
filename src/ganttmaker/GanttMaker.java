/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ganttmaker;

import javafx.scene.shape.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 *
 * @author blaze
 */
public class GanttMaker extends Application  {
    
    @Override
    public void start(Stage primaryStage){
      
        ListView<String> typeList = new ListView<String>();
        typeList.getItems().addAll("Minut","Godzin","Dni");
        typeList.setMaxHeight(90);
        
        TextField wpiszDlugosc = new TextField();
        TextField wpiszNazwe = new TextField();
        
        Label textName = new Label("Wpisz nazwe zadania");
        Label textDlugosc = new Label("Wpisz dlugosc");
        Label textType = new Label("Wybierz typ czasu");
        Label error = new Label("Error");
        error.setVisible(false);
        error.setScaleX(3);
        error.setScaleY(3);
        error.setTranslateY(100);
        error.setTranslateX(-100); 
        
        Button buttonOk = new Button("Dodaj Zadanie");
        Button buttonAll = new Button("Wypisz Wsio");
        Button buttonMakeXML = new Button("Stworz XML");
        Button buttonTakeXML = new Button("Odtworz XML");
        Button buttonCreateChart = new Button("Rysuj Wykres");
        /*
        ScrollBar sc = new ScrollBar();
        sc.setMin(0);
        sc.setMax(100);
        sc.setValue(50);
        sc.setOrientation(Orientation.HORIZONTAL);
        */
        Tasks tasks = new Tasks();
        tasks.setTasks(new ArrayList<>());
        
        BorderPane mainPane = new BorderPane();
        
    //    List<Task> taskList = new ArrayList<Task>();
        
        EventHandler<javafx.scene.input.MouseEvent> addTaskEvent = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
                //ObservableList<String> types;
                //types = typeList.getSelectionModel().getSelectedItems();
                error.setVisible(false);
              
                try {
                
                //int dlInt = Integer.parseInt(wpiszDlugosc.getText());
               String types = typeList.getSelectionModel().getSelectedItem();
                    
               Task tasktemp = new Task();
               tasktemp.setLength(Integer.parseInt(wpiszDlugosc.getText()));
               tasktemp.setName(wpiszNazwe.getText()); 
               tasktemp.setTypeOfLength(types.toString());
               tasks.getTasks().add(tasktemp);
             
               
                wpiszDlugosc.clear();
                wpiszNazwe.clear();
                } catch (NumberFormatException ex) {
                    System.out.println("Jakis blad ");
                }
        } 
        };  
        
       
        EventHandler<javafx.scene.input.MouseEvent> checkTasksEvent = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
                
                int tasksSize = tasks.getTasks().size();
                System.out.println(tasksSize);
                for(int i=0;i<tasksSize;i++)
                {
                    System.out.println(tasks.getTasks().get(i).getLength());
                    System.out.println(tasks.getTasks().get(i).getTypeOfLength());
                    System.out.println(tasks.getTasks().get(i).getName());
                    System.out.println();
                }
        } 
        };  
         
         
        EventHandler<javafx.scene.input.MouseEvent> createXML = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
               
            
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Tasks.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                
                
                jaxbMarshaller.marshal(tasks, System.out);
                jaxbMarshaller.marshal(tasks, new File("C:/temp/employees.xml"));
                    
                
                
            } catch (JAXBException ex) {
               System.out.println("Jakis problem");
            }
                
        } 
        };  
        
       

        
        EventHandler<javafx.scene.input.MouseEvent> takeXML = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
                
                try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Tasks.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Tasks tasksm = (Tasks)jaxbUnmarshaller.unmarshal(new File("c:/temp/employees.xml"));
                // int size = 0;
               //  int index = 0; 
               
               int listSize = tasks.getTasks().size();
               int xmlSize = tasksm.getTasks().size();
               
               System.out.println(listSize); //0
               System.out.println(xmlSize);  //3
               
               boolean check = checkListAndXml(tasks,tasksm);  // true
               
               if (xmlSize > 0)
               {
                   if (listSize == 0)
                   {
                        /// dodajemy bez sprawdzania
                       for(Task tsk : tasksm.getTasks())
                       {
                       int taskmIndex = tasksm.getTasks().indexOf(tsk);  // 0 1 2
                       Task taskTemp = new Task();
                       taskTemp.setLength(tasksm.getTasks().get(taskmIndex).getLength());
                       taskTemp.setName(tasksm.getTasks().get(taskmIndex).getName());
                       taskTemp.setTypeOfLength(tasksm.getTasks().get(taskmIndex).getTypeOfLength());
                       tasks.getTasks().add(taskTemp);
                       System.out.println(tasks.getTasks().size());
                       //tasks.getTasks().add(new Task());
                       //tasks.getTasks().get(listSize).setLength(tsk.getLength()); 
                       //tasks.getTasks().get(listSize).setName(tsk.getName());
                       //tasks.getTasks().get(listSize).setTypeOfLength(tsk.getTypeOfLength());
                       //System.out.println(listSize); 
                       }
                   }
                   else if (listSize > 0)
                   {
                       if (check == false)
                       {
                           for(Task tsk : tasksm.getTasks())
                            {
                            int taskmIndex = tasksm.getTasks().indexOf(tsk);  // 0 1 2
                            Task taskTemp = new Task();
                            taskTemp.setLength(tasksm.getTasks().get(taskmIndex).getLength());
                            taskTemp.setName(tasksm.getTasks().get(taskmIndex).getName());
                            taskTemp.setTypeOfLength(tasksm.getTasks().get(taskmIndex).getTypeOfLength());
                            tasks.getTasks().add(taskTemp);
                       }
                       }
                   }
               
               }
               else if (xmlSize == 0)
               {
                   System.out.println("Nie ma nic do dodania");
               }
               else System.out.println("Nieoczekiwany blad");
                // get size-1  (juz dodalismy wiec jest ok)
                // funkcja ktora sprawdzi czy w tasksm i tasks jest to samo
                // jesli tasksm jest pusty (if na caly kod) - nic nie robimy
                // jesli tasks jest pusty - dodajemy wszysrko - bez sprawdzania funkcją
                // jesli nie są puste - funkcja sprawdza czy nie sa takie same -
                // jesli nie sa to dodaje wszystko, jesli są, nic sie nie wykona
               
                } catch (JAXBException ex) {
                System.out.println("Jakis problem");
                }
        } 
        }; 
 
        //StackPane stack = new StackPane();
        AnchorPane anchorPane = new AnchorPane();
        //ObservableList list = anchorPane.getChildren();  
        EventHandler<javafx.scene.input.MouseEvent> createChart = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
               
               //Group rectangles = new Group();
               //Group text = new Group();
               //Pane rectPane = new Pane();
               int x=100,y=100;
               int width;
               int height=20;
               anchorPane.getChildren().clear();
               //ArrayList<Rectangle> rectList = new ArrayList<>();
               //ArrayList<Text> textList = new ArrayList<>();
               //rectList.clear();
               //textList.clear();
               //text.getChildren().removeAll(textList);
               //rectangles.getChildren().removeAll(rectList);
               
               
               int tasksSize = tasks.getTasks().size();
               for(int i=0;i<tasksSize;i++)
               {   
                width=tasks.getTasks().get(i).getLength();
                Rectangle rect = new Rectangle(x,y,width,height);
                rect.setFill(Color.CADETBLUE);
                anchorPane.getChildren().add(rect);
                
                anchorPane.getChildren().add(new Text(x+width,y+15,tasks.getTasks().get(i).getName()+ " - " + tasks.getTasks().get(i).getLength()+ " " + tasks.getTasks().get(i).getTypeOfLength()));
                   
                //textList.add(new Text(,y,tasks.getTasks().get(i).getName()));
               // textList.get(i).setX(rectList.get(i).getX());
              //  textList.get(i).setY(y);   
                
                y=y+20;    
                x=x+width;
               }
               
            //   textList.forEach((tex) -> {
            //       text.getChildren().add(tex);
            //   });
               
           //    rectList.forEach((rec) -> {
           //        rec.setFill(Color.RED);
           //        rectangles.getChildren().add(rec);
            //    });
               
               
               //stack.getChildren().addAll(rectangles,text);
               mainPane.setCenter(anchorPane);
               
        }
        };
        
        
        
        
        buttonOk.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, addTaskEvent);
        buttonAll.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, checkTasksEvent);
        buttonMakeXML.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, createXML);
        buttonTakeXML.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, takeXML);
        buttonCreateChart.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, createChart);
        
        VBox vBoxLeft = new VBox(textName,wpiszNazwe,textDlugosc,wpiszDlugosc,textType,typeList,buttonOk,buttonAll,buttonMakeXML,buttonTakeXML,buttonCreateChart);
        VBox vBoxRight = new VBox(error);

        
        mainPane.setLeft(vBoxLeft);
        mainPane.setCenter(vBoxRight);
        

        Scene scene = new Scene(mainPane, 300, 250);
        
        primaryStage.setTitle("Hello Gantt!");
        primaryStage.setScene(scene);
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        }
       
    
     public boolean checkListAndXml(Tasks t,Tasks m)
     {
            boolean check = true;
            int xmlSize = t.getTasks().size();  // 3
            int listSize = m.getTasks().size(); // 0
            
            if((listSize > 0) && (xmlSize > 0))
            {
            if(listSize > xmlSize)
            {
            for(int i=0;i<t.getTasks().size();i++)
            {
                if (!t.getTasks().get(i).getName().equals(m.getTasks().get(i).getName()))
                {
                    check=false;
                }
            }
            }
            else
            {
            for(int i=0;i<m.getTasks().size();i++)
            {
                if (!m.getTasks().get(i).getName().equals(t.getTasks().get(i).getName()))
                {
                    check=false;
                }
            }
            }
            }
            return check;
     }
    
     public static void main(String[] args) {
        launch(args);
    }

        
    }



@XmlRootElement(name = "Zadanie")
@XmlAccessorType (XmlAccessType.FIELD)
class Task
{
   
    private int length;
    private String typeOfLength;
    private String name;
    
    public int getLength() {return length;}
    public String getTypeOfLength() {return typeOfLength;}
    public String getName() {return name;}
    public void setLength(int l){length=l;}
    public void setTypeOfLength(String t){typeOfLength=t;}
    public void setName(String n){name=n;}

}

@XmlRootElement(name = "Zadania")
@XmlAccessorType (XmlAccessType.FIELD)
class Tasks
{
    @XmlElement(name = "Zadanie")
    private List<Task> zadania = null;
 
    public List<Task> getTasks() {
        return zadania;
    }
 
    public void setTasks(List<Task> zadania) {
        this.zadania = zadania;
    }
}


  

       
