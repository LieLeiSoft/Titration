package de.abakus;

   import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	
	/********************************************************************
	 *************** Funktion Runden ************************************
	 ********************************************************************
	 */
	
	private double fktRunden(double Zahl, int AnzahlStellen) {
		long Dummy =  (long) Math.pow(10, AnzahlStellen);
		double Ergebnis = (long) ((Zahl*Dummy) + (0.5*Math.signum(Zahl))) / (Dummy*1.0); // *1.0 damit's ein double wird!
		
		return Ergebnis;		
	}

	/******************************************************************* 
	 *************** Funktion - Textstring wird kopiert ****************
	 ******* str = Text ***** times = Anzahl der Wiederholungen ********
	 ******************************************************************/
	
	public static String fktStringRepeat(String str, int times){
		return new String(new char[times]).replace("\0", str);
	}
	
	/*******************************************************************
	 *************** Funktion Ergebnis aufbereiten *********************
	 ********* Anzahl Vorkommastelle / Anzahl Nachkommastelle **********
	 ******************************************************************/
	
	
	public String fktErgebnisAufbereiten(double dblErgebnis) {
		String strErgebnis;
        int intAnzahl_VK
           ,intAnzahl_NK;
		double dblMin
		      ,dblMax;
    	
    	NumberFormat formatter = new DecimalFormat("#0.#########"); // max. bis zu 9 Nachkommastellen vorsehen

    	intAnzahl_VK = 6; // max. Anzahl Vorkommastellen
		intAnzahl_NK = 9; // max. Anzahl Nachkommastellen
		
		dblMin = 1 / Math.pow(10, (intAnzahl_NK - 1));
		dblMax = Math.pow(10, intAnzahl_VK) - (1 / Math.pow(10, (intAnzahl_NK)));
		
    	strErgebnis = "";
    	
        // es wird schon hier gerundet, damit z.B. 1875,00000000002 später als glatte Zahl erkannt wird
    	dblErgebnis = fktRunden(dblErgebnis, intAnzahl_NK); // auf x Nachkommastellen runden

    	if ((dblErgebnis > 0) && (dblErgebnis < dblMin)) { 
    		strErgebnis = "<" + formatter.format(dblMin);
    	}
    	else if (dblErgebnis > dblMax) { 
    		strErgebnis = ">" + Double.toString(dblMax);
    	}
    	else if ((dblErgebnis == 0) || (dblErgebnis - Math.floor(dblErgebnis)) == 0) {    	
    		// Ergebnis ist 0 oder eine glatte Zahl
    		strErgebnis = Integer.toString((int) dblErgebnis);
    		strErgebnis = fktStringRepeat(" ", ((intAnzahl_VK+1)-strErgebnis.length())) + strErgebnis; //intAnzahl_VK+1 wegen "<" bzw. ">" an erster Stelle!
    	}
    	else {    	
    		// Ergebnis enthält Nachkommastellen
    		// Zahlenformat explizit setzen, um Exponentialschreibweise zu verhindern (Bsp.: 5.0E-4 ==> 0.5)
    		strErgebnis = formatter.format(dblErgebnis);
    	}

    	// Punkt durch Komma ersetzen. Dadurch ist sichergestellt, dass im Ergebnis kein Punkt als Dezimaltrennzeichen steht.
    	strErgebnis = strErgebnis.replace(".", ",");
    	if (strErgebnis.indexOf(",") > 0) {
    		String text = fktStringRepeat(" ",(intAnzahl_VK+1)) + strErgebnis + fktStringRepeat(" ",intAnzahl_NK);
    		strErgebnis = text.substring(text.indexOf(",") - (intAnzahl_VK+1), text.indexOf(",") + (intAnzahl_NK+1)); // intAnzahl_NK+1 weil "," berücksichtigt werden muss!
    	}
    	    	
    	return strErgebnis;
	} // private String fktErgebnisAufbereiten(double dblErgebnis)
	
	
	  // *******************************************************************************
	  // ** Hier wird verhindert, dass das Programm ohne Inhalt eines Feldes abstürzt **
	  // *******************************************************************************
	
		private double fktBlankToNumber(String Eingabetext){
	    	double Ergebnis = 0;
	    	if (Eingabetext.equals("") == false) {
	           	Ergebnis = Double.parseDouble(Eingabetext);
       		}
			return Ergebnis;		
	    }
		
	  /************************************************************
       ** Hier startet das Hauptpropramm **************************
       ************************************************************/
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abakus_main);

      
        } 
        
    public void btnRechneOnClickHandler(View v) {
    

    	/**************************************************************
    	 ******* ButtonRechner Variablen wird deklariert **************
    	 **************************************************************/
   
    	EditText et;
    	TextView tv;
      
    	String Eingabetext
    	      ,strTiter
    	      ,strTiter_1
    	      ,strTiter_2
    	      ,strTiter_3
    	      ,strTiter_4
    	      ,strAbweichung
    	      ;
    	
        double dblErgebnis = 0
        	  ,dblAbweichung = 0
              ,dblUrtiter = 0
    	      ,dblTiter = 0
    	      ,dblTiter_1 = 0
    	      ,dblTiter_2 = 0
    	      ,dblTiter_3 = 0
    	      ,dblTiter_4 = 0
     	      ,dblEinwaage_1 = 0
     	      ,dblEinwaage_2 = 0 
     	      ,dblEinwaage_3 = 0
     	      ,dblEinwaage_4 = 0
     	      ,dblVerbrauch_1 = 0
     	      ,dblVerbrauch_2 = 0
     	      ,dblVerbrauch_3 = 0
     	      ,dblVerbrauch_4 = 0
    	      ,dblFaktor = 0;
    	
        
    	et = (EditText) findViewById(R.id.Urtiter); 
    	et.requestFocus();   
    	Eingabetext = et.getText().toString();
    	dblUrtiter = fktBlankToNumber(Eingabetext);
    	
        et = (EditText) findViewById(R.id.Faktor);
    	Eingabetext = et.getText().toString();
    	dblFaktor = fktBlankToNumber(Eingabetext);
    	
    	et = (EditText) findViewById(R.id.Einwaage_1);
    	Eingabetext = et.getText().toString();
    	dblEinwaage_1 = fktBlankToNumber(Eingabetext);
    	
    	et = (EditText) findViewById(R.id.Einwaage_2);
    	Eingabetext = et.getText().toString();
    	dblEinwaage_2 = fktBlankToNumber(Eingabetext);
    	
    	et = (EditText) findViewById(R.id.Einwaage_3);
    	Eingabetext = et.getText().toString();
    	dblEinwaage_3 = fktBlankToNumber(Eingabetext);
    	
    	et = (EditText) findViewById(R.id.Einwaage_4);
    	Eingabetext = et.getText().toString();
    	dblEinwaage_4 = fktBlankToNumber(Eingabetext);
    	
    	et = (EditText) findViewById(R.id.Verbrauch_1);
    	Eingabetext = et.getText().toString();
    	dblVerbrauch_1 = fktBlankToNumber(Eingabetext);
    	
    	et = (EditText) findViewById(R.id.Verbrauch_2);
    	Eingabetext = et.getText().toString();
    	dblVerbrauch_2 = fktBlankToNumber(Eingabetext);
    	
    	et = (EditText) findViewById(R.id.Verbrauch_3);
    	Eingabetext = et.getText().toString();
    	dblVerbrauch_3 = fktBlankToNumber(Eingabetext);
    	
    	et = (EditText) findViewById(R.id.Verbrauch_4);
    	Eingabetext = et.getText().toString();
    	dblVerbrauch_4 = fktBlankToNumber(Eingabetext);
    	
    	    	
    	// Ergebnisfelder initialisieren
        dblErgebnis = 0;
    	
    	if (dblFaktor != 0) {
    		    	    		      
    	if ((dblEinwaage_1 != 0) && (dblVerbrauch_1 != 0)) {
    		dblErgebnis = (dblEinwaage_1 * dblUrtiter) / (dblVerbrauch_1 * dblFaktor * 100);
    		dblErgebnis  = fktRunden(dblErgebnis , 4);
    		dblTiter_1 = dblErgebnis;
    		strTiter_1 = Double.toString(dblErgebnis);
        	tv = (TextView) findViewById(R.id.tvErgebnis1);
        	tv.setText("t1= " + strTiter_1);
    	}
    	
    	if ((dblEinwaage_2 != 0) && (dblVerbrauch_1 != 0)) {
    		dblErgebnis = (dblEinwaage_2 * dblUrtiter) / (dblVerbrauch_2 * dblFaktor * 100);
    		dblErgebnis = fktRunden(dblErgebnis, 4);
    		dblTiter_2 = dblErgebnis;
    		strTiter_2 = Double.toString(dblErgebnis);
        	tv = (TextView) findViewById(R.id.tvErgebnis2);
        	tv.setText("t2= " + strTiter_2);
    	}
    	
        if ((dblEinwaage_3 != 0) && (dblVerbrauch_1 != 0)) {
        	dblErgebnis = (dblEinwaage_3 * dblUrtiter) / (dblVerbrauch_3 * dblFaktor * 100);
    		dblErgebnis = fktRunden(dblErgebnis, 4);
    		dblTiter_3 = dblErgebnis;
    		strTiter_3 = Double.toString(dblErgebnis);
        	tv = (TextView) findViewById(R.id.tvErgebnis3);
        	tv.setText("t3= " + strTiter_3);
        }
        
        if ((dblEinwaage_4 != 0) && (dblVerbrauch_1 != 0)) {
            dblErgebnis = (dblEinwaage_4 * dblUrtiter) / (dblVerbrauch_4 * dblFaktor * 100);
    		dblErgebnis = fktRunden(dblErgebnis, 4);
    		dblTiter_4 = dblErgebnis;
    		strTiter_4 = Double.toString(dblErgebnis);
        	tv = (TextView) findViewById(R.id.tvErgebnis4);
        	tv.setText("t4= " + strTiter_4);
        }
        
        // ***********************************************************
        // ******** Mittelwert bei 3 Titer *************************** 
        // ***********************************************************
        
        if (dblTiter_1 != 0 && dblTiter_2 != 0 && dblTiter_3 != 0 && dblTiter_4 == 0) {
        	dblErgebnis = (dblTiter_1 + dblTiter_2 + dblTiter_3) / 3;
        	dblErgebnis = fktRunden(dblErgebnis, 4);
        	dblTiter = dblErgebnis;
    		strTiter = Double.toString(dblTiter);
        	tv = (TextView) findViewById(R.id.tvErgebnis5);
        	tv.setText("Øt= " + strTiter);
        }	
        
        // ***********************************************************
        // ******** Mittelwert bei 4 Titer *************************** 
        // ***********************************************************
        
        if (dblTiter_1 != 0 && dblTiter_2 != 0 && dblTiter_3 != 0 && dblTiter_4 != 0) {
        	dblErgebnis = (dblTiter_1 + dblTiter_2 + dblTiter_3 + dblTiter_4) / 4;
        	dblErgebnis = fktRunden(dblErgebnis, 4);
        	dblTiter = dblErgebnis;
    		strTiter = Double.toString(dblTiter);
        	tv = (TextView) findViewById(R.id.tvErgebnis5);
        	tv.setText("Øt= " + strTiter);
        }	
        
        // ***********************************************************
        // ******** %RSD bei 3 Titer *************************** 
        // ***********************************************************
        
        if (dblTiter_1 != 0 && dblTiter_2 != 0 && dblTiter_3 != 0 && dblTiter_4 == 0) {
            dblTiter = (dblTiter_1 + dblTiter_2 + dblTiter_3) / 3;
            dblErgebnis = ((Math.sqrt((Math.pow((dblTiter_1 - dblTiter),2) + Math.pow((dblTiter_2 - dblTiter),2) + Math.pow((dblTiter_3 - dblTiter),2)) / 2)) * 100) / dblTiter;
        	dblErgebnis = fktRunden(dblErgebnis, 2);
        	dblAbweichung = dblErgebnis;
    		strAbweichung = Double.toString(dblAbweichung);
        	tv = (TextView) findViewById(R.id.tvErgebnis6);
        	tv.setText("RSD= "+strAbweichung+" %");
        }	
        
        // ***********************************************************
        // ******** %RSD bei 4 Titer *************************** 
        // ***********************************************************
                        
        if (dblTiter_1 != 0 && dblTiter_2 != 0 && dblTiter_3 != 0 && dblTiter_4 != 0) {
            dblTiter = (dblTiter_1 + dblTiter_2 + dblTiter_3 + dblTiter_4) / 4;
            dblErgebnis = ((Math.sqrt((Math.pow((dblTiter_1 - dblTiter),2) + Math.pow((dblTiter_2 - dblTiter),2) + Math.pow((dblTiter_3 - dblTiter),2) + Math.pow((dblTiter_4 - dblTiter),2)) / 3)) * 100) / dblTiter;
        	dblErgebnis = fktRunden(dblErgebnis, 2);
        	dblAbweichung = dblErgebnis;
    		strAbweichung = Double.toString(dblAbweichung);
        	tv = (TextView) findViewById(R.id.tvErgebnis6);
        	tv.setText("RSD= "+strAbweichung+" %");
        }	
        ;
        }
    
		// Anzeige der numerischen Tastatur ausschalten, um mehr Anzeigeplatz zu haben
    	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	if (getCurrentFocus() != null) {
    	    imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
    	}
    } // public void btnRechneOnClickHandler(View v)
    
    public void btnClearOnClickHandler(View v) {
    	
        // Eingabefelder zurücksetzen
    	    	  	    	
    	EditText e1 = (EditText) findViewById(R.id.Einwaage_1);
		e1.setText("");
		
    	EditText e2 = (EditText) findViewById(R.id.Einwaage_2);
		e2.setText("");
		
    	EditText e3 = (EditText) findViewById(R.id.Einwaage_3);
		e3.setText("");
		
    	EditText e4 = (EditText) findViewById(R.id.Einwaage_4);
		e4.setText("");
		
    	EditText v1 = (EditText) findViewById(R.id.Verbrauch_1);
		v1.setText("");
		
    	EditText v2 = (EditText) findViewById(R.id.Verbrauch_2);
		v2.setText("");
		
    	EditText v3 = (EditText) findViewById(R.id.Verbrauch_3);
		v3.setText("");
		
    	EditText v4 = (EditText) findViewById(R.id.Verbrauch_4);
		v4.setText("");
		
        // Ausgabefelder (Ergebnisfelder) zurücksetzen

    	TextView tv = (TextView) findViewById(R.id.tvErgebnis1);
    	tv.setText("Titer neu");
    	
    	// 'tv' muss nicht noch einmal als TextView deklariert werden, weil bereits oben geschehen.
    	tv = (TextView) findViewById(R.id.tvErgebnis2);
    	tv.setText("Titer neu");
    	
    	tv = (TextView) findViewById(R.id.tvErgebnis3);
    	tv.setText("Titer neu");
    	
    	tv = (TextView) findViewById(R.id.tvErgebnis4);
    	tv.setText("Titer neu");
    
		// Cursor in erstes Eingabefeld setzen und numerische Tastatur einschalten 
		e1 = (EditText) findViewById(R.id.Einwaage_1);
		e1.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(e1, InputMethodManager.SHOW_FORCED);
        
    } // public void btnClearOnClickHandler(View v)
    
    public void btnClearAllOnClickHandler(View v) {
    	
        // Eingabefelder zurücksetzen
    	    
    	EditText u1 = (EditText) findViewById(R.id.Urtiter);
		u1.setText("");
		
    	EditText f1 = (EditText) findViewById(R.id.Faktor);
		f1.setText("");
    	
    	EditText e1 = (EditText) findViewById(R.id.Einwaage_1);
		e1.setText("");
		
    	EditText e2 = (EditText) findViewById(R.id.Einwaage_2);
		e2.setText("");
		
    	EditText e3 = (EditText) findViewById(R.id.Einwaage_3);
		e3.setText("");
		
    	EditText e4 = (EditText) findViewById(R.id.Einwaage_4);
		e4.setText("");
		
    	EditText v1 = (EditText) findViewById(R.id.Verbrauch_1);
		v1.setText("");
		
    	EditText v2 = (EditText) findViewById(R.id.Verbrauch_2);
		v2.setText("");
		
    	EditText v3 = (EditText) findViewById(R.id.Verbrauch_3);
		v3.setText("");
		
    	EditText v4 = (EditText) findViewById(R.id.Verbrauch_4);
		v4.setText("");
		
        // Ausgabefelder (Ergebnisfelder) zurücksetzen

    	TextView tv = (TextView) findViewById(R.id.tvErgebnis1);
    	tv.setText("Titer");
    	
    	// 'tv' muss nicht noch einmal als TextView deklariert werden, weil bereits oben geschehen.
    	tv = (TextView) findViewById(R.id.tvErgebnis2);
    	tv.setText("Titer");
    	
    	tv = (TextView) findViewById(R.id.tvErgebnis3);
    	tv.setText("Titer");
    	
    	tv = (TextView) findViewById(R.id.tvErgebnis4);
    	tv.setText("Titer");
    	
		// Cursor in erstes Eingabefeld setzen und numerische Tastatur einschalten 
		u1 = (EditText) findViewById(R.id.Urtiter);
		u1.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(e1, InputMethodManager.SHOW_FORCED);
        
    } // public void btnClearOnClickHandler(View v)
}