package com.masy.pokeapp.service;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class Mp4Service {

    static final String FOLDER_PATH = "E:\\TorrentDownloads\\social media\\Udemy - Ultimate SEO, Social Media, & Digital Marketing Course 2022";

    public void mergeSrtToMp4ForFolders(){
        File f = new File(FOLDER_PATH);
        Arrays.asList(Objects.requireNonNull(f.listFiles()))
                .forEach(file ->
                        mergeSrtToMp4InFolder(file.getPath())
                );
    }

    public void mergeSrtToMp4InFolder(String folderPath){

        System.out.println("start for path " + folderPath);

        // get list of mp4 files from directory
        File folder = new File(folderPath);
        List<String> allVids = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .map(File::getName)
                .filter(name -> name.contains(".mp4"))
                .collect(Collectors.toCollection(ArrayList::new));

        allVids.forEach(v -> tryToAddSubtitles(v, folderPath));


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
        }
    }

    private void addSubsAndSaveFile(String pathSrt, String pathVideo, String pathOut) {
        try {
            TextTrackImpl subs = parse(new FileInputStream(pathSrt));
            Movie videoMovie = MovieCreator.build(new FileDataSourceImpl(new File(pathVideo)));

            addSubtitlesToMp4(videoMovie, subs);
            writeOutputMovie(pathOut, videoMovie);
            System.out.println("done file " + pathOut);
        }catch (Exception e){
            // log?
            System.out.println("oops "+ pathOut);
        }
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

        String n = r.readLine();
        while (n != null && n.length() == 1) {
            n = r.readLine();
        }
        while (n != null && n.length()>1) {
            String timeString = r.readLine();
            StringBuilder lineString = new StringBuilder();
            String s;
            while (!((s = r.readLine()) == null || s.trim().equals(""))) {
                lineString.append(s).append("\n");
            }
            long startTime = parseTimeToMillis(StringUtils.trimToEmpty(timeString.split("-->")[0]));
            long endTime = parseTimeToMillis(StringUtils.trimToEmpty(timeString.split("-->")[1]));
            track.getSubs().add(new TextTrackImpl.Line(startTime, endTime, lineString.toString()));
        }
        return track;
    }

    public long parseTimeToMillis(String time) {
        String seconds = time.split(",")[0];
        String millis = time.split(",")[1];
        String[] times = seconds.split(":");
        long result = Long.parseLong(millis);
        result += Long.parseLong(times[2])*1000; // secs to millis
        result += Long.parseLong(times[1])*1000*60; // mins to millis
        result += Long.parseLong(times[0])*1000*60*60; // hrs to millis
        return result;
    }
}
