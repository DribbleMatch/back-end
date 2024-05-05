package com.sideProject.DribbleMatch.common.module;

import com.sideProject.DribbleMatch.dto.region.RegionCsvDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Transactional
public class CsvScheduleWriter implements ItemWriter<RegionCsvDto> {

    private final RegionRepository regionRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends RegionCsvDto> chunk) throws Exception {
        Chunk<Region> regions = new Chunk<>();

        chunk.forEach(regionCsvDto -> {
            Region region = RegionCsvDto.of(regionCsvDto);
            regions.add(region);
        });

        regionRepository.saveAll(regions);
    }
}
