package com.sideProject.ClutchShot.common.module;

import com.sideProject.ClutchShot.dto.region.RegionCsvDto;
import com.sideProject.ClutchShot.entity.region.Region;
import com.sideProject.ClutchShot.repository.region.RegionRepository;
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
