-- airway_design スキーマの全テーブルを TRUNCATE する SQL
TRUNCATE TABLE 
    airway_design.railway_crossing_info
    ,airway_design.mapping_droneport_section
    ,airway_design.mapping_junction_section
    ,airway_design.airway_junction
    ,airway_design.airway_section
    ,airway_design.airway
    ,airway_design.fall_space
    ,airway_design.despersion_node
    ,airway_design.airway_compatible_models
    ,airway_design.airway_determination
    ,airway_design.fall_tolerance_range
    ,airway_design.locations;
