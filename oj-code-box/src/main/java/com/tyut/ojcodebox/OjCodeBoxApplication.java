package com.tyut.ojcodebox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class OjCodeBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjCodeBoxApplication.class, args);
        log.info("\n ________       ___  ___      ________      ________      _______       _______       ________     \n" +
                "|\\   ____\\     |\\  \\|\\  \\    |\\   ____\\    |\\   ____\\    |\\  ___ \\     |\\  ___ \\     |\\   ___ \\    \n" +
                "\\ \\  \\___|_    \\ \\  \\\\\\  \\   \\ \\  \\___|    \\ \\  \\___|    \\ \\   __/|    \\ \\   __/|    \\ \\  \\_|\\ \\   \n" +
                " \\ \\_____  \\    \\ \\  \\\\\\  \\   \\ \\  \\        \\ \\  \\        \\ \\  \\_|/__   \\ \\  \\_|/__   \\ \\  \\ \\\\ \\  \n" +
                "  \\|____|\\  \\    \\ \\  \\\\\\  \\   \\ \\  \\____    \\ \\  \\____    \\ \\  \\_|\\ \\   \\ \\  \\_|\\ \\   \\ \\  \\_\\\\ \\ \n" +
                "    ____\\_\\  \\    \\ \\_______\\   \\ \\_______\\   \\ \\_______\\   \\ \\_______\\   \\ \\_______\\   \\ \\_______\\\n" +
                "   |\\_________\\    \\|_______|    \\|_______|    \\|_______|    \\|_______|    \\|_______|    \\|_______|\n" +
                "   \\|_________|                                                                                    \n" +
                "                                                                                       ");
    }

}
