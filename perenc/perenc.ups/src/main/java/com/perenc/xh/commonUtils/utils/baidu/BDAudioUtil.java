package com.perenc.xh.commonUtils.utils.baidu;

import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.speech.AipSpeech;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class BDAudioUtil {
    public static final String APP_ID = "15461281";
    public static final String API_KEY = "yQQwiwnY9QPDFSnhGQAr9Wre";
    public static final String SECRET_KEY = "V2lkENLc2OEHEuadqGOHy6Twg2jV0Cwb";

    private static BDAudioUtil audioUtils = null;
    private BDAudioUtil(){}

    //双判断，解决单利问题
    public static BDAudioUtil getInstance(){
        if(audioUtils == null){
            synchronized (BDAudioUtil.class) {
                if(audioUtils == null){
                    audioUtils = new BDAudioUtil();
                }
            }
        }
        return audioUtils;
    }

    /**
     * MP3转换PCM文件方法
     * @param mp3filepath
     * @param pcmfilepath
     * @return
     */
    public AudioInputStream convertMP32Pcm(String mp3filepath, String pcmfilepath){
        try { //获取文件的音频流，pcm的格式
            AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
            InputStream inputStream = new FileInputStream(mp3filepath);
            //将音频转化为  pcm的格式保存下来
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(pcmfilepath));
            return audioInputStream;
        } catch (Exception e) {
            // TODO Auto-generated catch block e.printStackTrace();
            return null;
        }
    }

    /**
     * MP3转换PCM文件方法
     * @param mp3filepath
     * @param pcmfilepath
     * @return
     */
    public String convertMP32PcmText(String mp3filepath, String pcmfilepath){
        String result="";
        try { //获取文件的音频流，pcm的格式
            AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
            if(audioInputStream!=null) {
                //将音频转化为  pcm的格式保存下来
                File pcmFile = new File(pcmfilepath);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, pcmFile);
                if (pcmFile.exists()) {
                    AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
                    JSONObject res = client.asr(pcmfilepath, "pcm", 16000, null);
                    //JSONObject res = client.asr(input2byte(audioInputStream), "pcm", 16000, null);
                    if (res != null) {
                        String strresult = String.valueOf(res.get("result"));
                        JSONArray resultJsarry = JSONArray.parseArray(strresult);
                        if (resultJsarry.size() > 0) {
                            result = resultJsarry.get(0).toString();
                        }
                    }
                } else {
                    result = "";
                }
            }else {
                result = "";
            }
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block e.printStackTrace();
            result="";
        }
        return result;
    }

    /**
     * MP3转换PCM文件方法
     * @param mp3file
     * @param pcmfilepath
     * @return
     */
    public AudioInputStream convertMP3PcmFile(MultipartFile mp3file, String pcmfilepath){
        try { //获取文件的音频流，pcm的格式
            //AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
            AudioInputStream audioInputStream = getPcmAudioInputStreamFile(mp3file);
            //将音频转化为  pcm的格式保存下来
           // AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(pcmfilepath));
            return audioInputStream;
        } catch (Exception e) {
            // TODO Auto-generated catch block e.printStackTrace();
            return null;
        }
    }

    /**
     * 播放MP3方法
     * @param mp3filepath
     * @throws Exception
     */
    public static void playMP3(String mp3filepath) throws Exception {
        //获取音频为pcm的格式
        AudioInputStream audioInputStream = getPcmAudioInputStream(mp3filepath);
        // 播放
        if (audioInputStream == null){
            System.out.println("null audiostream");
            return;
        }
        //获取音频的格式
        AudioFormat targetFormat = audioInputStream.getFormat();
        DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, targetFormat, AudioSystem.NOT_SPECIFIED);
        //输出设备
        SourceDataLine line = null;
        try {
            line = (SourceDataLine) AudioSystem.getLine(dinfo);
            line.open(targetFormat);
            line.start(); int
                    len = -1;
            //            byte[] buffer = new byte[8192];
            byte[] buffer = new byte[1024];
            //读取音频文件
            while ((len = audioInputStream.read(buffer)) > 0) {
                //输出音频文件
                line.write(buffer, 0, len);
            }
            // Block等待临时数据被输出为空
            line.drain();
            //关闭读取流
            audioInputStream.close();
            //停止播放
            line.stop();
            line.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("audio problem " + ex);
        }
    }

    /**
     * 机能概要:获取文件的音频流
     * @param mp3filepath
     * @return
     */
    private static AudioInputStream getPcmAudioInputStream(String mp3filepath) {
        File mp3 = new File(mp3filepath); AudioInputStream audioInputStream = null;
        AudioFormat targetFormat = null;
        try {
            AudioInputStream in = null;
            //读取音频文件的类
            MpegAudioFileReader mp = new MpegAudioFileReader();
            in = mp.getAudioInputStream(mp3); AudioFormat baseFormat = in.getFormat();
            //设定输出格式为pcm格式的音频文件
            targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            //输出到音频
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioInputStream;
    }

    /**
     * 机能概要:获取文件的音频流
     * @param mp3file
     * @return
     */
    private static AudioInputStream getPcmAudioInputStreamFile(MultipartFile mp3file) {
        //File mp3 = new File(mp3filepath);
        AudioInputStream audioInputStream = null;
        //File mp3=mp3file.getInputStream();
        AudioFormat targetFormat = null;
        try {
            AudioInputStream in = null;
            //读取音频文件的类
            MpegAudioFileReader mp = new MpegAudioFileReader();
            in = mp.getAudioInputStream(mp3file.getInputStream());
            AudioFormat baseFormat = in.getFormat();
            //设定输出格式为pcm格式的音频文件
            targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            //输出到音频
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audioInputStream;
    }



    /*public static void main(String[] args) {
        System.out.println("====开始=====");
        String mp3path= "D:\\logs\\sound\\output1.mp3";
        String pcmpath= "D:\\logs\\sound\\pcm\\output1.pcm";
        try {
            //AudioUtils.playMP3(mp3path);

            AudioUtils.convertMP32Pcm(mp3path, pcmpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



}