
//import com.emotix.arya.expression.ConditionExpression;
import com.emotix.mikodb.mikoSQLDB;
import com.fasterxml.jackson.core.type.TypeReference;
//import com.emotix.arya.expression.ConditionExpression;
import com.emotix.jackson.MikoObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import com.emotix.arya.expression.ConditionExpression;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

        

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user4
 */
@WebServlet(urlPatterns = {"/image_audio"})
public class image_audio extends HttpServlet{
    
    private mikoSQLDB sqldb = mikoSQLDB.getsqlDBInstance();
     String ttsdata = "";
     String tdata="";
     String expr = "";
      String response = "", output = "";
      int has_exp = 0;
//     UserDetails userDetails = DM.getUserDetails(username);
    
    

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //To change body of generated methods, choose Tools | Templates.
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String jsonResp=req.getParameter("jsonResp");
        try
        {
            if (jsonResp.contains("###CE")) {
                        //System.out.println("ce object is : "+resp);
                        //resp = "{\"###CE\":[{\"delay\":\"\",\"rgb\":\"RGB2\",\"motion\":\"MOTION3\",\"sound\":\"SOUND1\",\"text\":\"TTS Hello all hello\",\"image\":\"IMG2\"}]}";
                        //Log.i(TAG, "The ###CE response object is : " + resp);
                        has_exp = 1;
                        Map<String, List<ConditionExpression>> data = MikoObjectMapper.OBJECT_MAPPER.readValue(jsonResp,
                                new TypeReference<Map<String, Object>>() {
                        });
                        List<ConditionExpression> CEexpressions = data.get("###CE");
                        String image, rgb, sound, motion, delay, expression, dx;
                        image = rgb = sound = motion = delay = expression = dx = "";

//                        Iterator<String> translation_iterator = getTranslationOutputGlossary(CEexpressions, userDetails,sess_info).iterator();

                        //System.out.println("JSON array length is : "+a.length());
                        //System.out.println("JSON array length is : "+a.length());
                        for (ConditionExpression CEexpression : CEexpressions) {

                            String[] expressions = CEexpression.getAllConditionExpres();

                            Map<String, String> allexpression = sqldb.getExpression(expressions);
                            if (allexpression != null && allexpression.size() != 0) {
                                sound = replace_block_exp(allexpression.getOrDefault(CEexpression.getsound(), ""));

                                image = replace_block_exp(allexpression.getOrDefault(CEexpression.getimage(), ""));

                                motion = replace_block_exp(allexpression.getOrDefault(CEexpression.getmotion(), ""));

                                rgb = replace_block_exp(allexpression.getOrDefault(CEexpression.getRGB(), ""));
                            }
                            dx = CEexpression.getDX();
                            ttsdata = CEexpression.gettext().trim();
                            if (!ttsdata.equals("")) {
//                                ttsdata = get_tts_resp_plain(translation_iterator.next().trim(), username,sess_info);
                                String[] tts_array = ttsdata.split("\\$\\$\\$\\$");
                                ttsdata = tts_array[0];
                                if (tts_array.length > 1) {
                                    tdata = tts_array[1];
                                }
                            }

//                            expression += create_expression(ttsdata, tdata, image, rgb, sound, motion, username, dx);

                            delay = CEexpression.getDelay();
                            if (!delay.equals("")) {
                                expression += "<expression>delay=" + delay + "</expression>";
                            }
                        }
                        //System.out.println("\n\n\nThe expr is after the loop is :\n\n\n "+expression);
                        expr = "<block>" + expression + "</block>";
                        output += " " + expr.trim();
                        //System.out.println("\n\n\nThe final "+output+"\n\n\n");
                    } else {
                        Map<String, String> expression = sqldb.getExpression("EXPRESSION0");
                        expr = expression.get("EXPRESSION0");
                    }

                    if (has_exp == 0) {
                        //Log.e(TAG, "expression: " + expr + " data: " + tdata, username);

//                        ttsdata = get_tts_resp(tdata.trim(), username,sess_info);
                        //split on ~~ 
                        String[] tts_array = ttsdata.split("\\$\\$\\$\\$");
                        ttsdata = tts_array[0];
                        if (tts_array.length > 1) {
                            tdata = tts_array[1];
                        }
//                        tdata = resp_extract1(expr, ttsdata, tdata, "", sess_info);//resp_extract1(expr,ttsdata,tdata);
                        if (tdata == null) {
                            //                    resp += "";
                            //Log.e(TAG, "could not process xml so response directly passed to addoutput without expression", username);
                        } else {
                            output += " " + tdata;
                            //Log.e(TAG, " response in json " + output, username);
                        }
                    }
                    response = output.trim();
                    setResponse(resp, response);
                
//                long end2 = System.currentTimeMillis();
                
//                Log.i(TAG, "whole time take for for loop "+(end2-start2)+"ms");
    
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
}
     public void setResponse(HttpServletResponse response, String data) {
        try {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer;
            writer = response.getWriter();
            writer.append(data);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
     
     public String replace_block_exp(String exp) {
        exp = exp.replace("<block>", "").replace("<expression>", "").replace("</block>", "").replace("</expression>", "").replace("<\\block>", "").replace("<\\expression>", "").trim();
        return exp;
    }
}
  

    

    

