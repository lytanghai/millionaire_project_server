package com.millionaire_project.millionaire_project.service;

import com.millionaire_project.millionaire_project.entity.EconomicEventEntity;
import com.millionaire_project.millionaire_project.repository.EconomicEventRepository;
import com.millionaire_project.millionaire_project.util.DateUtil;
import com.millionaire_project.millionaire_project.util.ResponseBuilder;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SourceOfNewsService {

    @Autowired private EconomicEventRepository repository;

    public void uploadExcel(MultipartFile file) throws Exception {
        List<EconomicEventEntity> events = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row (start at row 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // skip empty rows

                Cell firstCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (firstCell == null || firstCell.toString().trim().isEmpty()) {
                    break;
                }

                EconomicEventEntity event = new EconomicEventEntity();

                Cell eventCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                event.setEvent(eventCell != null ? eventCell.toString() : "");

                Cell dateCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (dateCell != null) {
                    event.setSchedule(DateUtil.convertToPhnomPenhDate(dateCell.getDateCellValue()));
                }

                Cell expectedCell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                event.setExpected(expectedCell != null ? expectedCell.toString() : "");

                event.setCreatedAt(DateUtil.convertToPhnomPenhDate(new Date()));

                events.add(event);
            }

            repository.saveAll(events);
        }
    }

    public ResponseBuilder<List> response() {
            String month = DateUtil.getCurrentYearMonth()[1];
            String year = DateUtil.getCurrentYearMonth()[0];

        List<EconomicEventEntity> result = repository.findAllByMonthAndYearNative(Integer.parseInt(month), Integer.parseInt(year));

        return ResponseBuilder.success(result);
    }
}
