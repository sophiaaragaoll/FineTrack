package ufrn.finetrack.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ufrn.finetrack.model.Transaction;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersistenceService {

    private final Path filePath; 
    private final Gson gson;

    public PersistenceService(String fileLocation) {
        this.filePath = Path.of(fileLocation);

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }
    
    
    //Carrega o arquivo JSON e converte para lista de Transaction.
  
    public List<Transaction> load() {
        try {
            // Se o arquivo não existir, retorna lista vazia
            if (!Files.exists(filePath)) {
                return new ArrayList<>();
            }

            String json = Files.readString(filePath);

            Type listType = new TypeToken<List<Transaction>>() {}.getType();
            return gson.fromJson(json, listType);

        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    //Salva a lista de Transaction em um arquivo JSON.
  
    public void save(List<Transaction> transactions) {
        try {
            // Garante que o diretório existe
            Files.createDirectories(filePath.getParent());

            String json = gson.toJson(transactions);

            // Escreve no arquivo
            Files.writeString(filePath, json);

        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo JSON: " + e.getMessage());
        }
    }
}
