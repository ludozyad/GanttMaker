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
import javafx.scene.shape.Line;
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
 * @author blazej
 */
public class GanttMaker extends Application  {
    
    @Override
    public void start(Stage primaryStage){
      
        // ListView z widokiem typów czasów
        ListView<String> typeList = new ListView<String>();  
        typeList.getItems().addAll("Minut","Godzin","Dni");
        typeList.setMaxHeight(90);
        
        // ListView z widokiem zadań
        ListView tasksList = new ListView();
        tasksList.setMaxHeight(100);
        
        TextField wpiszDlugosc = new TextField();  // Pole do wpisywania czasu
        TextField wpiszNazwe = new TextField();  // Pole do wpisywania nazwy
        
        // Labele stanowiące oznaczenia wyboru zadania
        Label textName = new Label("Wpisz nazwe zadania");
        Label textDlugosc = new Label("Wpisz dlugosc");
        Label textType = new Label("Wybierz typ czasu");
        
        // Deklaracje buttonów
        Button buttonOk = new Button("Dodaj Zadanie");
        Button buttonAll = new Button("Wypisz zadania");
        Button buttonMakeXML = new Button("Stworz XML");
        Button buttonTakeXML = new Button("Odtworz XML");
        Button buttonCreateChart = new Button("Rysuj Wykres");
        Button buttonChildTask = new Button("Zadanie Potomek");

        // Utworzenie obiektu klasy Tasks, która tworzy liste złożoną z obiektów klasy Task
        Tasks tasks = new Tasks();
        tasks.setTasks(new ArrayList<>());
        
        // Utworzenie głównego layoutu
        BorderPane mainPane = new BorderPane();
        
        // Obsługa przycisku dodawania zadania
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
        
        // Obsługa przycisku wypisywania zadań
        EventHandler<javafx.scene.input.MouseEvent> checkTasksEvent = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
                
                int tasksSize = tasks.getTasks().size();
                for(int i=0;i<tasksSize;i++)
                {
                    System.out.println("Nazwa Zadania: " + tasks.getTasks().get(i).getName());
                    System.out.println("Długość Zadania: " + tasks.getTasks().get(i).getLength());
                    System.out.println("Typ długości: " + tasks.getTasks().get(i).getTypeOfLength());
                    
                    if((tasks.getTasks().get(i).getIsParallel()) == true)
                    {
                        System.out.println("Zadanie jest potomkiem innego zadania");
                    }
                    else
                    {
                        System.out.println("Zadanie nie jest potomkiem innego zadania");
                    } 
                    System.out.println(" ----------------------------- ");
                }
                 tasksList.getItems().clear();
                 
                 // Dodanie zadań do ListView
                 for(int i=0; i<tasks.getTasks().size(); i++){
                     tasksList.getItems().add(tasks.getTasks().get(i).getName());
                 }
        }   
        };  
         
        // Obsługa przycisku tworzenia pliku XML
        EventHandler<javafx.scene.input.MouseEvent> createXML = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
               
            
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Tasks.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                               
                jaxbMarshaller.marshal(tasks, System.out);
                jaxbMarshaller.marshal(tasks, new File("C:\\Users\\blaze\\Documents\\NetBeansProjects\\GanntMaker\\tasks.xml"));
                          
            } catch (JAXBException ex) {
               System.out.println("Problem z plikiem XML");
            }
                
        } 
        };  

        // Obsługa przycisku odczytywania danych z pliku XML
        EventHandler<javafx.scene.input.MouseEvent> takeXML = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
                
                try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Tasks.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Tasks tasksm = (Tasks)jaxbUnmarshaller.unmarshal(new File("C:\\Users\\blaze\\Documents\\NetBeansProjects\\GanntMaker\\tasks.xml"));
               
               int listSize = tasks.getTasks().size();
               int xmlSize = tasksm.getTasks().size();
               
               System.out.println(listSize); 
               System.out.println(xmlSize);  
               
               boolean check = checkListAndXml(tasks,tasksm);  
               
               if (xmlSize > 0)
               {
                   if (listSize == 0)
                   {
                       for(Task tsk : tasksm.getTasks())
                       {
                       int taskmIndex = tasksm.getTasks().indexOf(tsk);  
                       Task taskTemp = new Task();
                       taskTemp.setLength(tasksm.getTasks().get(taskmIndex).getLength());
                       taskTemp.setName(tasksm.getTasks().get(taskmIndex).getName());
                       taskTemp.setTypeOfLength(tasksm.getTasks().get(taskmIndex).getTypeOfLength());
                       if(tasksm.getTasks().get(taskmIndex).getIsParallel() == true)
                       {
                           taskTemp.setIsParallel(true);
                       }
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
                            int taskmIndex = tasksm.getTasks().indexOf(tsk);  
                            Task taskTemp = new Task();
                            taskTemp.setLength(tasksm.getTasks().get(taskmIndex).getLength());
                            taskTemp.setName(tasksm.getTasks().get(taskmIndex).getName());
                            taskTemp.setTypeOfLength(tasksm.getTasks().get(taskmIndex).getTypeOfLength());
                            if(tasksm.getTasks().get(taskmIndex).getIsParallel() == true)
                            {
                                  taskTemp.setIsParallel(true);
                            }
                                  tasks.getTasks().add(taskTemp);
                            }
                       }
                   }       
               }
               else if (xmlSize == 0)
               {
                   System.out.println("Nie ma nic do dodania");
               }
               } catch (JAXBException ex) {
               System.out.println("Problem z plikiem XML");
               }
        } 
        }; 
 
        // Utworzenie layoutu do obsługi wykresu
        AnchorPane anchorPane = new AnchorPane();
        ScrollPane scrollpane = new ScrollPane(anchorPane);
        scrollpane.setPrefSize(600, 200);
        
        // Obsługa przycisku utworzenia wykresu
        EventHandler<javafx.scene.input.MouseEvent> createChart = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
               
               List<Integer> tab = new ArrayList();
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
                        int prevWidth=0;
                        if("Minut".equals(tasks.getTasks().get(i-1).getTypeOfLength()))
                        {
                            prevWidth=tasks.getTasks().get(i-1).getLength();
                        }
                        else if("Godzin".equals(tasks.getTasks().get(i-1).getTypeOfLength()))
                        {
                             prevWidth=tasks.getTasks().get(i-1).getLength()*60;
                        }
                        else if("Dni".equals(tasks.getTasks().get(i-1).getTypeOfLength()))
                        {
                            prevWidth=tasks.getTasks().get(i-1).getLength()*60*24;
                        }
                  
                   if(tasks.getTasks().get(i-1).getIsParallel()==true)
                   {   
                        int size = tab.size();
                        System.out.println(size);
                        Rectangle rect = new Rectangle(tab.get(size-1),y,width,height);
                        rect.setFill(Color.CADETBLUE);   
                        anchorPane.getChildren().add(rect);
                        anchorPane.getChildren().add(new Text(tab.get(size-1)+width,y+15,tasks.getTasks().get(i).getName()+ " - " + tasks.getTasks().get(i).getLength()+ " " + tasks.getTasks().get(i).getTypeOfLength()));
                   }
                   else 
                    {
                        tab.add(x-prevWidth);
                        int size = tab.size();
                        System.out.println(size);
                        Rectangle rect = new Rectangle(tab.get(size-1),y,width,height);
                        rect.setFill(Color.CADETBLUE);
                        anchorPane.getChildren().add(rect);
                        anchorPane.getChildren().add(new Text(tab.get(size-1)+width,y+15,tasks.getTasks().get(i).getName()+ " - " + tasks.getTasks().get(i).getLength()+ " " + tasks.getTasks().get(i).getTypeOfLength()));
                    }
               }
               else
               {
                   Rectangle rect = new Rectangle(x,y,width,height);
                   rect.setFill(Color.CADETBLUE);
                   anchorPane.getChildren().add(rect);
                   anchorPane.getChildren().add(new Text(x+width,y+15,tasks.getTasks().get(i).getName()+ " - " + tasks.getTasks().get(i).getLength()+ " " + tasks.getTasks().get(i).getTypeOfLength())); 
                   x=x+width;
               }
               y=y+22;       
               }
               int overallength = 0;
               for (int j = 0; j < tasksSize; j++)
               {
                        if("Minut".equals(tasks.getTasks().get(j).getTypeOfLength()))
                        {
                            overallength = overallength + tasks.getTasks().get(j).getLength()/60;
                        }
                        else if("Godzin".equals(tasks.getTasks().get(j).getTypeOfLength()))
                        {
                             overallength = overallength + tasks.getTasks().get(j).getLength();
                        }
                        else if("Dni".equals(tasks.getTasks().get(j).getTypeOfLength()))
                        {
                            overallength = overallength + tasks.getTasks().get(j).getLength()*24;
                        }
               }
               int start = 100;
               for (int k = 0; k < overallength + 1; k++)
               {
                   Text text = new Text("+ " + k);
                   text.setX(start+5);
                   text.setY(30);
                   text.setFill(Color.CADETBLUE);
                   Line line = new Line();
                   line.setStartX(start);
                   line.setEndX(start);
                   line.setEndY(600);
                   line.setStrokeWidth(0.8);
                   line.setStroke(Color.LIGHTBLUE);

                   anchorPane.getChildren().add(line);
                   anchorPane.getChildren().add(text);
                   start = start + 60;
               }
               mainPane.setCenter(scrollpane);
        }};
        
        // Obsługa przycisku dodawania zadania potomnego
        EventHandler<javafx.scene.input.MouseEvent> childNode = 
        new EventHandler<javafx.scene.input.MouseEvent>() { 
        @Override 
        public void handle(javafx.scene.input.MouseEvent e) { 
               
            if((!wpiszDlugosc.getText().isEmpty()&&!wpiszNazwe.getText().isEmpty()&&!typeList.getSelectionModel().isEmpty()))
            {
                System.out.println("All gra");
                
                if(!tasksList.getSelectionModel().isEmpty())
                {
                    int index = tasksList.getSelectionModel().getSelectedIndex();
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
        }
        };  

        // Przypisanie obsługi przycisków do przycisków
        buttonOk.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, addTaskEvent);
        buttonAll.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, checkTasksEvent);
        buttonMakeXML.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, createXML);
        buttonTakeXML.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, takeXML);
        buttonCreateChart.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, createChart);
        buttonChildTask.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, childNode);
        
        // Utworzenie layoutu zawierającego elementy służące do interakcji z aplikacją
        VBox vBoxLeft = new VBox(textName,wpiszNazwe,textDlugosc,wpiszDlugosc,textType,typeList,buttonOk,buttonAll,buttonMakeXML,buttonTakeXML,buttonCreateChart,tasksList,buttonChildTask);
        mainPane.setLeft(vBoxLeft);

        Scene scene = new Scene(mainPane, 300, 250);
        
        primaryStage.setTitle("Hello Gantt!");
        primaryStage.setScene(scene);
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);
        primaryStage.setResizable(true);
        primaryStage.show();
        
        }
       
     // Funkcja sprawdzająca czy plik xml i baza zadań nie pokrywają się
     public boolean checkListAndXml(Tasks t,Tasks m)
     {
            boolean check = true;
            int xmlSize = t.getTasks().size();  
            int listSize = m.getTasks().size(); 
            
            if((listSize > 0) && (xmlSize > 0))
            {
            if(listSize > xmlSize)
            {
            for(int i=0;i<t.getTasks().size();i++)
            {
                if (!t.getTasks().get(i).getName().equals(m.getTasks().get(i).getName()))
                {
                    check = false;
                }
            }
            }
            else
            {
            for(int i=0; i < m.getTasks().size(); i++)
            {
                if (!m.getTasks().get(i).getName().equals(t.getTasks().get(i).getName()))
                {
                    check = false;
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

// Klasa pojedynczego zadania
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

// Klasa listy zadań 
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