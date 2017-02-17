package ru.mit.spbau.antonpp.messanger.network;

import lombok.Builder;
import lombok.Data;

/**
 * Simple Message that has only two fields.
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
@Data
@Builder
public class SignedMessage {
    private final String sender;
    private final String text;
}
