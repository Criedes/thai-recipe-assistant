package dev.crides.poc.devassist.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import dev.crides.poc.devassist.service.ChatService;
import org.vaadin.firitin.components.messagelist.MarkdownMessage;
import org.vaadin.firitin.components.messagelist.MarkdownMessage.Color;

import java.util.Optional;

@Route("")
public class ThaiRecipeAssistanceView extends VerticalLayout {

    public ThaiRecipeAssistanceView(ChatService chatService) {
        String chatId = chatService.establishChat();
        var messageList = new VerticalLayout();
        var messageInput = new MessageInput();
        messageInput.setWidthFull();
        messageInput.addSubmitListener(e -> submitPromptListener(chatId, e, chatService, messageList));
        addClassName("centered-content");
    }

    private static void submitPromptListener(String chatId, MessageInput.SubmitEvent e, ChatService chatService, VerticalLayout messageList) {
        var question = e.getValue();
        var userMessage = new MarkdownMessage(question, "You", Color.AVATAR_PRESETS[6]);
        var assistantMessage = new MarkdownMessage("Assistant", Color.AVATAR_PRESETS[0]);
        messageList.add(userMessage, assistantMessage);
        chatService.chat(chatId, question)
                .map(res -> Optional.ofNullable(res.getResult().getOutput().getContent()).orElse(""))
                .subscribe(assistantMessage::appendMarkdownAsync);
    }
}
