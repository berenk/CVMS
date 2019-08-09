package com.example.emergencyalert;

import com.google.gson.Gson;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.util.ArrayList;

public class WebServiceCallerImpl implements WebServiceCaller {
    private static final String NAME_SPACE="http://ekampus.akdeniz.edu.tr/webservice/";
    private static final String SERVICE_URL="http://ekampus.akdeniz.edu.tr/webservis/Webservis/personelAD.asmx";
    private static final String DOGRULA_METOD="ADPersonelkullanicisimi";
    private static final String SOAP_DOGRULA_ACTION=NAME_SPACE + "ADPersonelkullanicisimi";

    @Override
    public boolean PersonelDogrula(PersonelParameters input) {
        SoapObject request=new SoapObject(NAME_SPACE,DOGRULA_METOD);
        request.addProperty("DeVtok",input.getDeVtokBytes());
        request.addProperty("YoneticiAdi",input.getYoneticiAdi());
        request.addProperty("YoneticiSifre",input.getYoneticiSifre());

        System.out.println(request.getProperty(0));
        System.out.println(request.getProperty(1));
        System.out.println(request.getProperty(2));



        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);
        /*
        //Header ayarları
        ArrayList<HeaderProperty> headerProperties = new ArrayList<>();
        headerProperties.add(new HeaderProperty("KullaniciAdi",input.getKullaniciAdiBytes()));
        headerProperties.add(new HeaderProperty("SicilNo",input.getSicilNoBytes()));
        headerProperties.add(new HeaderProperty("Sifresi",input.getSifresiBytes()));
        headerProperties.add(new HeaderProperty("ProjeId",input.getProjeIdBytes()));

        for (HeaderProperty haeder : headerProperties){
            System.out.println(haeder.getKey() + "\n" + haeder.getValue());
        }
        */

        envelope.headerOut = new Element[1];
        envelope.headerOut[0] = buildAuthHeader(new PersonelParameters());

        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);



        HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICE_URL);
        try{
            androidHttpTransport.call(SOAP_DOGRULA_ACTION,envelope);
            if(envelope.bodyIn instanceof SoapObject){
                SoapObject soapObject=(SoapObject) envelope.bodyIn;

                System.out.println(soapObject.getProperty(0));


                return false;
            }
            else if(envelope.bodyIn instanceof SoapFault){
                SoapFault soapFault=(SoapFault) envelope.bodyIn;
                System.out.println("Hata" + soapFault.getMessage());




            }
            }
        //E si buyuk:)
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
        return false;
    }

    private Element buildAuthHeader(PersonelParameters input) {


        Element AuthHeader = new Element().createElement(NAME_SPACE,"AuthHeader");

        Element KullaniciAdi = new Element().createElement(NAME_SPACE,"KullaniciAdi");
        Element SicilNo = new Element().createElement(NAME_SPACE, "SicilNo");
        Element Sifresi = new Element().createElement(NAME_SPACE, "Sifresi");
        Element ProjeId = new Element().createElement(NAME_SPACE, "ProjeId");

        KullaniciAdi.addChild(Node.TEXT,input.getKullaniciAdiBytes());
        SicilNo.addChild(Node.TEXT,input.getSicilNoBytes());
        Sifresi.addChild(Node.TEXT,input.getSifresiBytes());
        ProjeId.addChild(Node.TEXT,input.getProjeIdBytes());

        AuthHeader.addChild(Node.ELEMENT,KullaniciAdi);
        AuthHeader.addChild(Node.ELEMENT,SicilNo);
        AuthHeader.addChild(Node.ELEMENT,Sifresi);
        AuthHeader.addChild(Node.ELEMENT,ProjeId);





        return AuthHeader;



    }

}
