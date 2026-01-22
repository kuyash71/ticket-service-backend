package com.sau.ticket_service.service;

import com.sau.ticket_service.dto.TicketRequestDTO;
import com.sau.ticket_service.dto.TicketResponseDTO;
import com.sau.ticket_service.exception.TicketNotFoundException;
import com.sau.ticket_service.model.Ticket;
import com.sau.ticket_service.model.TicketStatus;
import com.sau.ticket_service.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public TicketResponseDTO create(TicketRequestDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(dto.getPriority());

        // default status (entity de zaten garantiye alıyor)
        ticket.setStatus(TicketStatus.OPEN);

        Ticket saved = ticketRepository.save(ticket);
        return TicketResponseDTO.from(saved);
    }

    @Transactional(readOnly = true)
    public TicketResponseDTO getById(Long id) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        return TicketResponseDTO.from(t);
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDTO> getAll() {
        return ticketRepository.findAll().stream()
                .map(TicketResponseDTO::from)
                .toList();
    }

    public TicketResponseDTO updateStatus(Long id, TicketStatus status) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        t.setStatus(status);
        Ticket saved = ticketRepository.save(t);
        return TicketResponseDTO.from(saved);
    }

    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
    }
}
