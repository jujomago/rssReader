import javax.microedition.midlet.*;

import org.kxml2.io.*;
import org.xmlpull.v1.*;

import javax.microedition.lcdui.*;
import javax.microedition.io.*;

import java.io.*;
import java.util.Vector;

public class principal extends MIDlet implements CommandListener  {
    
	private Form f1,f2;
	private TextField blog_edit;
	private List L_Entradas;
	private Alert enespera,Ayuda,alerta1;
	private Command R_titles,R_content,back_f2,salir,ayuda;
	private StringItem resultItem = new StringItem ("","");
	private StringItem ejemplo=new StringItem("","");
	private Vector Id_Entradas = new Vector();
	public principal() {
		// TODO Auto-generated constructor stub
		
		R_titles=new Command("Ir al Blog",Command.OK,1);
		R_content=new Command("Leer Entrada",Command.OK,1);
		back_f2=new Command("Ver Lista",Command.BACK,1);
		salir=new Command("salir",Command.BACK,0);
		ayuda=new Command("ayuda",Command.ITEM,1);
		
		f1=new Form("Lector de Blogs");
		blog_edit=new TextField("Introduce la Direccion de un Blog ::","",50,TextField.URL);
		blog_edit.setString("http://");
		ejemplo.setText("Ej: http://mundocoolf.blogspot.com/atom.xml");
		f1.append(blog_edit);
		f1.append(ejemplo);
		f1.addCommand (R_titles);
		f1.addCommand (ayuda);
		f1.setCommandListener (this);
		
		L_Entradas=new List("Seleccione una Entrada",List.IMPLICIT);
		L_Entradas.addCommand(R_content);
		L_Entradas.addCommand(salir);
		L_Entradas.setCommandListener(this);
		f2=new Form("Contenido");
		f2.append(resultItem);
		f2.addCommand(back_f2);
		f2.setCommandListener(this);
		
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		Display.getDisplay (this).setCurrent(f1);
	}

	public void commandAction(Command c, Displayable d) {
		// TODO Auto-generated method stub
		   if (c == R_titles) {
			    if(validar_url(blog_edit.getString())){
    		       ReadXML inicio=new ReadXML(blog_edit.getString(),"Lista");
			       inicio.start();
			       Display.getDisplay(this).setCurrent(mostrarespera(),L_Entradas);
        		  }
			    else
			    	showmessage();
             }
		   if (c==ayuda){
			   String texto="Esta aplicación lee feeds de tipo ATOM, los utilzados por los blogs de blogger, "+
			   	  	        "en la pantalla inicial debe introducir la dirección url completa del feed ó "+
			                "solo la direccion de la página, pero la pagina debe tener sindicación atom "+
			                "\nEjemplos: Para entrar a mi blog pueden colocar http://mundocoolf.blogspot.com o "+
			                "http://mundocoolf.blogspot.com/atom.xml\n "+
			                "Nota: No esta probado con paginas que no sean blogs, pero puede ser que en algunos casos funcione." +
			                "Utiliza conexion a internet"+
			                "\n\n Desarrollado por: Josue Mancilla G. \n Librerias Utilizadas: Kxml2";
			                
               Ayuda = new Alert( "Funcionamiento",texto,null, AlertType.INFO); 
			   Ayuda.setTimeout(Alert.FOREVER);
			   Display.getDisplay(this).setCurrent(Ayuda);
			   
		   }
		   if(c == R_content){
			   ReadXML inicio=new ReadXML(blog_edit.getString(),"Contenido");
			   inicio.start();
			   /*Image img=null;
			      try {
			    	  img = Image.createImage("/cargando.gif");
			      } catch (IOException e) {
					// TODO Auto-generated catch block
			    	  System.err.println("Error: " + e);
				  }
			    ImageItem imagen= new ImageItem("Espere mientras se carga el post...",img,ImageItem.LAYOUT_CENTER,"texto alternativo");
			    f2.append(imagen);*/
			    //f2.setTitle("cargando..");
			    Display.getDisplay(this).setCurrent(f2);
     	   }
		   if(c==back_f2){
			   this.resultItem.setText("");
			   Display.getDisplay(this).setCurrent(L_Entradas);
     	   }
		   if(c==salir){
			try {
				   	  destroyApp(false);
				      notifyDestroyed(); 
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     	   }
		
	}
	private boolean validar_url(String url) {
		// TODO Auto-generated method stub
		if(url.endsWith(".com")||url.endsWith(".net")||url.endsWith(".org"))
		{
		  if(url.endsWith("atom.xml")==false)
			 blog_edit.setString(blog_edit.getString()+"/atom.xml");  
			 return true;  
		}
		else 
		return false;
	}

	private Alert mostrarespera(){
		Image logo = null;
		try {logo = Image.createImage("/wireless.png" );   }
	    catch( IOException e ){}
	    enespera = new Alert( "Conectando....","\n\nPorfavor espere",logo, null ); 
	    enespera.setTimeout(15000);
		return enespera;
	}
	private void showmessage() {
		// TODO Auto-generated method stub
  	  alerta1 = new Alert("Error","La dirección url es incorrecta, revise los ejemplos",null,AlertType.ERROR);
	  alerta1.setTimeout(3000);
	  Display.getDisplay(this).setCurrent(alerta1);
		
	}
	public class ReadXML extends Thread {
		String URLBlog="";
		String proceso="";
		
	    //StringItem resultItem = new StringItem ("", "");
	    StringBuffer sb=null;
	   
        public ReadXML(String url,String proceso){
        	this.URLBlog=url;
        	this.proceso=proceso;
    	}
	    public void run() {
			try {
				 //Open http connection
			     HttpConnection httpConnection = (HttpConnection) Connector.open(URLBlog);
		 		 //Initilialize XML parser
		    	 KXmlParser parser = new KXmlParser();
		    	 parser.setInput(new InputStreamReader(httpConnection.openInputStream()));
				 if(proceso.equals("Lista")){
					 
					 //System.out.println("entro");
					 sb=new  StringBuffer();
        				 while (parser.next() != XmlPullParser.END_DOCUMENT) {
					    	String name=parser.getName();
						      if (parser.getEventType() == XmlPullParser.START_TAG) {
						    	  //String text=parser.nextText(); // no va, por que no se sabe si el tag va tener sub tags
						    	  if(name.equals("id")){
						    		Id_Entradas.addElement(parser.nextText());
						    		//sb.append("ID : ");
						     	    //sb.append(parser.nextText());
						    	 }
						    	 if(name.equals("title")){
						    		String texto=parser.nextText();
						           //System.out.println("<title> "+name+","+parser.nextText()+"</title>");
						    	   	sb.append("Titulo : ");
					     	    	sb.append(texto+"\n");
					     	    	L_Entradas.append(texto,null);
					     	    	
					     	      }
						      }
       					 }//End - While
        				 //System.out.println(L_Entradas.size());
        				 //System.out.println(sb.toString());
						  /*  for(int i= 0 ; i< Id_Entradas.size() ;i++){
				     	    	String entra2 = (String) Id_Entradas.elementAt(i);
				     	    	System.out.println("id"+entra2);		     	         	    	   	
				     	   }*/	
					 
				 }
				 else if(proceso.equals("Contenido")){
					  String ident_value=(String)Id_Entradas.elementAt(L_Entradas.getSelectedIndex());
					 //System.out.println("id="+ident_value);
					 sb=new  StringBuffer(); 
					 int band=0;
					    while (parser.next() != XmlPullParser.END_DOCUMENT) {
					    	String name=parser.getName();
						      if (parser.getEventType() == XmlPullParser.START_TAG) {
						    	  //String text=parser.nextText(); // no va, por que no se sabe si el tag va tener sub tags
						    	  if(name.equals("id")){
						    		  String texto=parser.nextText();
						    		   if(texto.equals(ident_value)){
						    			  // System.out.println("encontrado "+texto);
        							       band=1;
						    		  }
						    	  }
						    	  if(band==1){
						    	  if(name.equals("summary")||name.equals("content")){
									   //System.out.println("<summary> "+name+","+parser.nextText()+"</summary>");
							           //entr.setDescription(parser.nextText());
							    		  	sb.append(parser.nextText());
							    		  	sb.append("\n");
						     	     }
							    	 if(parser.getAttributeCount()>0){
							    	   if(parser.getAttributeValue(0).equals("alternate")&&parser.getAttributeName(2).equals("href")){
									      //System.out.println("<link href> "+name+","+parser.getAttributeValue(2)+"</link href>");
							    	    	sb.append("Link Directo : ");
							     	    	sb.append(parser.getAttributeValue(2));
							    	        
							    	      break;
							    	  }
							    	 }
						    	   }
			      		      }
					     }//End - While
					   
						 //String chtitulo=L_Entradas.getString(L_Entradas.getSelectedIndex());
						 //f2.setTitle(chtitulo);
						 resultItem.setText(sb.toString());
						 //System.out.println(sb.toString());
						 //new principal().resultItem.setLabel("Entradas");
						 //new principal().resultItem.setLabel("Entradas");
				     	 /*   for(int i= 0 ; i< EntradaVector.size() ;i++){
				     	    	Entradas entra2 = (Entradas) EntradaVector.elementAt(i);
				     	    	System.out.println("con SS: "+entra2.getName());
				     	    	//System.out.println(entra.getDescription());	     	    	   	
				     	      }*/	
				 }
	         }
			 catch (Exception e) {
		    	  	e.printStackTrace ();
		    		resultItem.setLabel ("Error al parsear el doc xml:");
		    		resultItem.setText (e.toString ());

			}
	     }
	}
}

