package com.itransition.anton.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FileLoader {
    private final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens";

    private final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private final String CREDENTIALS_FILE_PATH = "/client_secret.json";

    private Drive service;

    public FileLoader() {
        final NetHttpTransport HTTP_TRANSPORT;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String PROFILE_FOLDER_ID = "1SQjVT3jDgL0AyD1sihIwhf_8uh6HHGVp";

    public String sendFileByProfile(MultipartFile miltipartFile, String receiverName) {
        File fileMetadata = new File();
        List<String> parents = new ArrayList<>(1);
        parents.add(PROFILE_FOLDER_ID);
        fileMetadata.setParents(parents);
        fileMetadata.setName(receiverName);
        System.out.println("!!!" + miltipartFile.getOriginalFilename());
        java.io.File filePath = new java.io.File(miltipartFile.getOriginalFilename());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(miltipartFile.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";
        }

        System.out.println("!!!" + filePath);
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = null;
        try {
            file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            return "";
        }
        System.out.println("File ID: " + file.getId());
        return file.getId();
    }

    public String getFile(String fileId) {
        File file = null;
        try {
            file = service.files().get(fileId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file == null || file.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.printf("%s (%s)\n", file.getName(), file.getId());
            return file.getId();
        }
        return "";
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = FileLoader.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
        java.io.File convFile = new java.io.File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        FileLoader fl = new FileLoader();
    }
}
