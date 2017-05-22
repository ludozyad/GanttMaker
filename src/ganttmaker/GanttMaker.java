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
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
        
        Button buttonOk = new Button("Dodaj Zadanie");
        Button buttonAll = new Button("Wypisz Wsio");
        Button buttonMakeXML = new Button("Stworz XML");
        Button buttonTakeXML = new Button("Odtworz XML");
        Button buttonCreateChart = new Button("Rysuj Wykres");
        Button buttonChildTask = new Button("Zadanie Potomek");

        Tasks tasks = new Tasks();
        tasks.setTasks(new ArrayList<>());
        
        BorderPane mainPane = new BorderPane();
        
        ListView listView = new ListView();
        listView.setMaxHeight(100);
        
       
       
        
        
        EventHandler<javafx.scene.input.MouseEvent> addTaskEvent = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
              
              
                try {
                
                
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
                
                 listView.getItems().clear();
                 for(int i=0; i<tasks.getTasks().size(); i++){
                     listView.getItems().add(tasks.getTasks().get(i).getName());
                 }
        System.out.println();
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
                Tasks tasksm = (Tasks)jaxbUnmarshaller.unmarshal(new File("c:/temp/tasks.xml"));
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
     
                } catch (JAXBException ex) {
                System.out.println("Jakis problem");
                }
        } 
        }; 
 
        //StackPane stack = new StackPane();
        AnchorPane anchorPane = new AnchorPane();
        
        ScrollPane scrollpane = new ScrollPane(anchorPane);
        scrollpane.setPrefSize(600, 200);
        
        //ObservableList list = anchorPane.getChildren();  
        EventHandler<javafx.scene.input.MouseEvent> createChart = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
               
          
               int x=100,y=100;
               int width=0;
               int height=20;
               anchorPane.getChildren().clear();
     
               int tasksSize = tasks.getTasks().size();
               for(int i=0;i<tasksSize;i++)
               {   
                   
                if("Minut".equals(tasks.getTasks().get(i).getTypeOfLength()))
                {
                    width=tasks.getTasks().get(i).getLength();
                }
                else if("Godzin".equals(tasks.getTasks().get(i).getTypeOfLength()))
                {
                     width=tasks.getTasks().get(i).getLength()*60;
                }
                else if("Dni".equals(tasks.getTasks().get(i).getTypeOfLength()))
                {
                    width=tasks.getTasks().get(i).getLength()*60*24;
                }
                   
               if (tasks.getTasks().get(i).getIsParallel() == true)
               {
                 int prevWidth = 0;  
                   if("Minut".equals(tasks.getTasks().get(i-1).getTypeOfLength()))
                   {
                       prevWidth = tasks.getTasks().get(i-1).getLength();
                   }
                   else if("Godzin".equals(tasks.getTasks().get(i-1).getTypeOfLength()))
                   {
                       prevWidth = tasks.getTasks().get(i-1).getLength()*60;
                   }
                   else if("Dni".equals(tasks.getTasks().get(i-1).getTypeOfLength()))
                   {
                       prevWidth = tasks.getTasks().get(i-1).getLength()*60*24;
                   }
                   
                   Rectangle rect = new Rectangle(x - prevWidth ,y,width,height);
                   rect.setFill(Color.CADETBLUE);
                    anchorPane.getChildren().add(rect);
                    anchorPane.getChildren().add(new Text(x - prevWidth + width,y+15,tasks.getTasks().get(i).getName()+ " - " + tasks.getTasks().get(i).getLength()+ " " + tasks.getTasks().get(i).getTypeOfLength()));
               }
               else
               {

               // width=tasks.getTasks().get(i).getLength();
                Rectangle rect = new Rectangle(x,y,width,height);
                rect.setFill(Color.CADETBLUE);
                anchorPane.getChildren().add(rect);
                
                anchorPane.getChildren().add(new Text(x+width,y+15,tasks.getTasks().get(i).getName()+ " - " + tasks.getTasks().get(i).getLength()+ " " + tasks.getTasks().get(i).getTypeOfLength()));
                   

                y=y+2;    
                x=x+width;
               }
               y=y+20;   
               mainPane.setCenter(scrollpane);
              
               
        }
        }};
        
        /*
        
         String types = typeList.getSelectionModel().getSelectedItem();
                    
               Task tasktemp = new Task();
               tasktemp.setLength(Integer.parseInt(wpiszDlugosc.getText()));
               tasktemp.setName(wpiszNazwe.getText()); 
               tasktemp.setTypeOfLength(types.toString());
               tasks.getTasks().add(tasktemp);
        
        
        */
        
        
        EventHandler<javafx.scene.input.MouseEvent> childNode = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
               
            if((!wpiszDlugosc.getText().isEmpty()&&!wpiszNazwe.getText().isEmpty()&&!typeList.getSelectionModel().isEmpty()))
            {
                System.out.println("All gra");
                
                if(!listView.getSelectionModel().isEmpty())
                {
                    int index = listView.getSelectionModel().getSelectedIndex();
                    System.out.println(index);
                    for (int i=0; i<tasks.getTasks().size(); i++)
                    {
                        if(i == index)
                        {
                            if(index+1 != tasks.getTasks().size())
                            {
                                System.out.println("Jest nastepny");
                                Task tasktemp = new Task();
                                String types = typeList.getSelectionModel().getSelectedItem();
                                tasktemp.setLength(Integer.parseInt(wpiszDlugosc.getText()));
                                tasktemp.setName(wpiszNazwe.getText());
                                tasktemp.setTypeOfLength(types.toString());
                                tasks.getTasks().add(index+1, tasktemp);
                                tasktemp.setIsParallel(true);
                                wpiszDlugosc.clear();
                                wpiszNazwe.clear();
                            }
                            else
                            {
                                System.out.println("Jest ostatni");
                                String types = typeList.getSelectionModel().getSelectedItem();
                                Task tasktemp = new Task();
                                tasktemp.setLength(Integer.parseInt(wpiszDlugosc.getText()));
                                tasktemp.setName(wpiszNazwe.getText());
                                tasktemp.setTypeOfLength(types.toString());
                                tasktemp.setIsParallel(true);
                                tasks.getTasks().add(tasktemp);
                                wpiszDlugosc.clear();
                                wpiszNazwe.clear();
                            }
                        }
                    }
                   
                }
                else 
                {
                    System.out.println("Nie zaznaczyles zadania");
                }
            }
            else 
            {
                System.out.println("cos nie gra");
            }
            // zadanie bedzie polegało na tym,że znajdujemy zaznaczony indeks, wszystkie zadania po tym indeksie
            // przesuwamy o 1 pozycje do przodu a następnie po wybranym indeksie wstawiamy to co wpisaliśmy
            // należy zmodyfikować funkcje rysującą tak, ażeby zadanie z flagą isParallel było rysowane równolegle
            // z poprzednim, a zadanie następujące po nim rozpoczynało się kiedy skończy się zadanie poprzednie.
           String wybor = listView.getSelectionModel().getSelectedItems().toString();
           
        }
        };  

        buttonOk.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, addTaskEvent);
        buttonAll.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, checkTasksEvent);
        buttonMakeXML.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, createXML);
        buttonTakeXML.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, takeXML);
        buttonCreateChart.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, createChart);
        buttonChildTask.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, childNode);
        
        VBox vBoxLeft = new VBox(textName,wpiszNazwe,textDlugosc,wpiszDlugosc,textType,typeList,buttonOk,buttonAll,buttonMakeXML,buttonTakeXML,buttonCreateChart,listView,buttonChildTask);
        //VBox vBoxRight = new VBox();

        
        mainPane.setLeft(vBoxLeft);

        Scene scene = new Scene(mainPane, 300, 250);
        
        primaryStage.setTitle("Hello Gantt!");
        primaryStage.setScene(scene);
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);
        primaryStage.setResizable(true);
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
    private boolean isParallel = false;
    
    public int getLength() {return length;}
    public String getTypeOfLength() {return typeOfLength;}
    public String getName() {return name;}
    public boolean getIsParallel(){return isParallel;}
    public void setLength(int l){length=l;}
    public void setTypeOfLength(String t){typeOfLength=t;}
    public void setName(String n){name=n;}
    public void setIsParallel(boolean ip){isParallel=ip;}
   
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
// tableview - wybieranie zadan do ktorych dodajemy  przed/po (wiele zadan moze byc wykonywany rownoczesnie)
//
// siatka interwaly czasowe
  

       
