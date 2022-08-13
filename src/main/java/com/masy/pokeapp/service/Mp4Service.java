package com.masy.pokeapp.service;

import org.apache.commons.lang3.StringUtils;
import org.mp4parser.Container;
import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.container.mp4.MovieCreator;
import org.mp4parser.muxer.tracks.TextTrackImpl;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Mp4Service {


    public void mergeSrtToMp4ForFolders(){
        File f = new File("E:\\TorrentDownloads\\social media\\Udemy - Ultimate SEO, Social Media, & Digital Marketing Course 2022\\13. SUCCESS, FREELANCING & HIGH VALUE CLIENTS");

//        if (Arrays.stream(f.listFiles()).noneMatch(File::isDirectory)) {
            mergeSrtToMp4InFolder(f.getPath());
//        } else {
//            Arrays.asList(Objects.requireNonNull(f.listFiles()))
//                    .forEach(file ->
//                            mergeSrtToMp4InFolder(file.getPath())
//                    );
//        }

    }

    public void mergeSrtToMp4InFolder(String folderPath){

        System.out.println("start for path " + folderPath);

        // get list of mp4 files from directory
        File folder = new File(folderPath);
        List<String> allVids = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .map(File::getName)
                .filter(name -> name.contains(".mp4"))
                .collect(Collectors.toCollection(ArrayList::new));

        allVids.forEach(v ->
                tryToAddSubtitles(v, folderPath)
        );


        System.out.println("end for path " + folderPath);
    }


    public void tryToAddSubtitles(String vidName, String folderPath) {

        String srtName = vidName.substring(0, vidName.length()-4);
        String pathSrt = MessageFormat.format("{0}\\{1}.srt", folderPath, srtName);
        String pathVideo = MessageFormat.format("{0}\\{1}", folderPath, vidName);
        String pathOut = MessageFormat.format("{0}\\{1}\\{2}", folderPath, "merged", vidName);

        makeMergedDirectoryIfNotPresent(folderPath);

        if (!new File(pathOut).exists()){

            System.gc();
            System.runFinalization();
            addSubsAndSaveFile(pathSrt, pathVideo, pathOut);

        }

    }

    private void makeMergedDirectoryIfNotPresent(String folderPath) {
        File mergedFolder = new File(MessageFormat.format("{0}\\{1}", folderPath, "merged"));
        if (!mergedFolder.exists()){
            mergedFolder.mkdir();
            mergedFolder.setReadable(true);
            mergedFolder.setWritable(true);
            mergedFolder.setExecutable(true);

        }
    }

    private void addSubsAndSaveFile(String pathSrt, String pathVideo, String pathOut) {
        System.out.println("Parsing " + pathVideo);
        try {

            TextTrackImpl subs = parse(new FileInputStream(pathSrt));
            Movie videoMovie = MovieCreator.build(pathVideo);

            addSubtitlesToMp4(videoMovie, subs);
            writeOutputMovie(pathOut, videoMovie);

        }catch (Error | Exception e ){
            // log?
            System.out.println("--------------------OOOOOOOOOOOOPPPSSSSSSSSSSSSSS "+ pathOut);
        }
        System.out.println("done file " + pathOut);
    }

    private void writeOutputMovie(String pathOut, Movie videoMovie) throws IOException {
        Container out = new DefaultMp4Builder().build(videoMovie);
        FileOutputStream fos = new FileOutputStream(pathOut);

        out.writeContainer(fos.getChannel());
        fos.close();
    }


    public void addSubtitlesToMp4(Movie mp4, TextTrackImpl subs) {

        TextTrackImpl subTitleEng = new TextTrackImpl();
        subTitleEng.getTrackMetaData().setLanguage("eng");
        subTitleEng.getTrackMetaData().setLayer(0);
        subTitleEng.getSubs().addAll(subs.getSubs());
        mp4.addTrack(subTitleEng);

    }

    public TextTrackImpl parse(InputStream is) throws IOException {
        LineNumberReader r = new LineNumberReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        TextTrackImpl track = new TextTrackImpl();

        String timeString = r.readLine(); // prima riga
        while (timeString != null) {

            while (!timeString.contains("-->")){
                timeString = r.readLine();
                if (timeString == null)
                    return track;
            }

            StringBuilder lineString = new StringBuilder();
            String s;
            while ( !( (s = r.readLine()) == null || s.trim().equals("") || s.length()==1)) { // prende le righe da inserire per quel determinato momento
                lineString.append(s).append("\n");
            }
            long startTime = parseTimeToMillis(StringUtils.trimToEmpty(timeString.split("-->")[0])); // inizio sottotitolo
            long endTime = parseTimeToMillis(StringUtils.trimToEmpty(timeString.split("-->")[1])); // fine sottotitolo
            track.getSubs().add(new TextTrackImpl.Line(startTime, endTime, lineString.toString()));

            timeString = r.readLine(); // riga dopo il testo da inserire
        }
        return track;
    }

    public long parseTimeToMillis(String time) {
        if (time.split(",").length<2){
            System.out.println("STOP HERE MF");
        }

        String seconds = time.split(",")[0];
        String millis = time.split(",")[1];
        String[] times = seconds.split(":");
        long result = Long.parseLong(millis);
        result += Long.parseLong(times[2])*1000; // secs to millis
        result += Long.parseLong(times[1])*1000*60; // mins to millis
        result += Long.parseLong(times[0])*1000*60*60; // hrs to millis
        return result;
    }

    public void deleteAllSrt(String path) {

        File f = new File(path);

        File[] arr = f.listFiles();
        List<File> list = arr == null? Collections.emptyList() : Arrays.asList(arr);


    }


}
