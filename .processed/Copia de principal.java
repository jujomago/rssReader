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
	private Alert enespera;
	private Command R_titles,R_content,back_f2,back_f1;
	private StringItem resultItem = new StringItem ("","");
	private StringItem ejemplo=new StringItem("","");
	//static final String URL = "http://mundocoolf.blogspot.com/atom.xml";
	//static final String URL="http://sergiopixel.blogspot.com/atom.xml";
	private static final String URL="http://womenhome.blogspot.com/atom.xml";
	private Vector Id_Entradas = new Vector();
	private StringBuffer sb=null;
	public principal() {
		// TODO Auto-generated constructor stub
		
		R_titles=new Command("Ir al Blog",Command.OK,1);
		R_content=new Command("Leer Entrada",Command.OK,1);
		back_f2=new Command("Ver Lista",Command.BACK,1);
		back_f1=new Command("Inicio",Command.BACK,1);
		
		f1=new Form("Lector de Blogs");
		blog_edit=new TextField("Introduce la Direccion de un Blog ::","",50,TextField.URL);
		ejemplo.setText("Ej: http://mundocoolf.blogspot.com/atom.xml");
		f1.append(blog_edit);
		f1.append(ejemplo);
		f1.addCommand (R_titles);
		f1.setCommandListener (this);
		
		L_Entradas=new List("Seleccione una Entrada",List.IMPLICIT);
		L_Entradas.addCommand(R_content);
		L_Entradas.addCommand(back_f1);
		L_Entradas.setCommandListener(this);
		f2=new Form("Entrada");
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
			   
			   String urlsend=(this.blog_edit.getString().equals(""))?URL:blog_edit.getString();
			   if(L_Entradas.size()==0){
			   ReadXML inicio=new ReadXML(urlsend,"Lista");
			   inicio.start();
			   Display.getDisplay(this).setCurrent(mostrarespera(),this.L_Entradas);
			   }
			   else{
			   Display.getDisplay(this).setCurrent(this.L_Entradas);
			   }
             }
		   if(c == R_content){
			   ReadXML inicio=new ReadXML(URL,"Contenido");
			   inicio.start();
			   Image img=null;
			      try {
			    	  img = Image.createImage("/cargando.gif");
			      } catch (IOException e) {
					// TODO Auto-generated catch block
			    	  System.err.println("Error: " + e);
				  }
			    ImageItem imagen= new ImageItem("Espere mientras se carga el post...",img,ImageItem.LAYOUT_CENTER,"texto alternativo");
			    f2.append(imagen);
			   
			   Display.getDisplay(this).setCurrent(this.f2);
     	   }
		   if(c==back_f2){
			   Display.getDisplay(this).setCurrent(this.L_Entradas);
     	   }
		   if(c==back_f1){
			   Display.getDisplay(this).setCurrent(this.f1);
     	   }
		
	}
	public Alert mostrarespera(){
		Image logo = null;
	    try {logo = Image.createImage("/wireless.png" );   }
	    catch( IOException e ){}
	    enespera = new Alert( "Conectando....","Porfavor espere mientras se recibe información del Servidor",logo, null ); 
	    enespera.setTimeout(15000);
		return enespera;
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
					 int c=0;
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
					 //f2.setTitle(L_Entradas.getString(L_Entradas.getSelectedIndex()));
					 String ident_value=(String)Id_Entradas.elementAt(L_Entradas.getSelectedIndex());
					 //System.out.println("id="+ident_value);
					 sb=new  StringBuffer(); 
					 int band=0;
					 int cont=0;
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
					   
					     
						 resultItem.setText(sb.toString());
						 f2.delete(1);
						// System.out.println(sb.toString());
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
		    		resultItem.setLabel ("Error:");
		    		resultItem.setText (e.toString ());

			}
	     }
	}
}
