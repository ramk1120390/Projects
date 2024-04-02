
---------------------     metmodel update code -----------------------------------------                
CREATE OR REPLACE PROCEDURE insert(
    device_model_param VARCHAR,
    part_number_param VARCHAR,
    vendor_param VARCHAR,
    height_param REAL,
    depth_param REAL,
    width_param REAL,
    shelves_contained_param INTEGER,
    num_of_rack_position_occupied_param INTEGER,
    allowed_card_list_param VARCHAR[],
    INOUT  success INT -- Use INTEGER instead of BOOLEAN
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Attempt to insert the record
    INSERT INTO device_meta_model (device_model, part_number, vendor, height, depth, width, shelves_contained, num_of_rack_position_occupied, allowed_card_list)
    VALUES (
        device_model_param, 
        part_number_param, 
        vendor_param, 
        height_param, 
        depth_param, 
        width_param, 
        shelves_contained_param, 
        num_of_rack_position_occupied_param, 
        allowed_card_list_param
    )
    ON CONFLICT (device_model) DO UPDATE
    SET 
        part_number = EXCLUDED.part_number,
        vendor = EXCLUDED.vendor,
        height = EXCLUDED.height,
        depth = EXCLUDED.depth,
        width = EXCLUDED.width,
        shelves_contained = EXCLUDED.shelves_contained,
        num_of_rack_position_occupied = EXCLUDED.num_of_rack_position_occupied,
        allowed_card_list = EXCLUDED.allowed_card_list;

    -- Set output parameter to indicate success (1 for true, 0 for false)
    IF FOUND THEN
        success := 1;
    ELSE
        success := 0;
    END IF;

EXCEPTION
    WHEN others THEN
        -- If any exception occurs, set output parameter to indicate failure (0)
        success := 0;
        RAISE;
END;
$$;

---------------   end -------------------------------------------------------------------------------


CREATE OR REPLACE PROCEDURE insert_device(
    i_devicename VARCHAR,
    i_deviceModel VARCHAR,
    i_location VARCHAR,
    i_organisation VARCHAR,
    i_customer VARCHAR,
    i_managementIp VARCHAR,
    i_rackPosition VARCHAR,
    i_operationalState varchar,
    i_administrativeState VARCHAR,
    i_usageState VARCHAR,
    i_serialNumber VARCHAR,
    i_href VARCHAR,
    i_building VARCHAR,
    i_order_id BIGINT,
    i_realtion Varchar,
    INOUT success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    inserted_id BIGINT;
    shelf_count INT;
    shelf_position INT;
BEGIN
    -- Get the number of shelves contained based on device model
    SELECT shelves_contained INTO shelf_count FROM device_meta_model WHERE device_model = i_deviceModel;

    -- Begin transaction
    BEGIN
        -- Insert device record into device table
        INSERT INTO device (
            devicename,
            device_model,
            location,
            organisation,
            customer,
            management_ip,
            rack_position,
            operational_state,
            administrative_state,
            usage_state,
            serial_number,
            href,
            building_name,
            order_id,
            realtion
        )
        VALUES (
            i_devicename,
            i_deviceModel,
            i_location,
            i_organisation,
            i_customer,
            i_managementIp,
            i_rackPosition,
            i_operationalState,
            i_administrativeState,
            i_usageState,
            i_serialNumber,
            i_href,
            i_building,
            i_order_id,
            i_realtion
        )
        RETURNING id INTO inserted_id;

        -- Insert shelves associated with the device
        FOR shelf_position IN 1..shelf_count LOOP
            BEGIN
                INSERT INTO shelf (
                    name,
                    shelf_position,
                    operational_state,
                    administrative_state,
                    href,
                    usage_state,
                    devicename,
                    realtion
                )
                VALUES (
                    i_devicename || '_shelf_' || shelf_position,
                    shelf_position,
                    i_operationalState,
                    i_administrativeState,
                    i_href,
                    i_usageState,
                    i_devicename,
                    'device_to_shelf'
                );
            EXCEPTION
                WHEN others THEN
                    -- Rollback transaction if any error occurs
                    RAISE NOTICE 'Error inserting shelf. Rolling back transaction.';
                    ROLLBACK;
                    -- Return failure
                    success := 0;
                    RETURN;
            END;
        END LOOP;

        -- Commit transaction if everything is successful
        COMMIT;

        -- Return success
        success := 1;
    END;
END;
$$;

------------------    end -----------------------------------------------------------------------------


CREATE OR REPLACE PROCEDURE update_device(
    i_administrative_state VARCHAR,
    i_credentials VARCHAR,
    i_customer VARCHAR,
    i_device_model VARCHAR,
    i_devicename VARCHAR,
    i_href VARCHAR,
    i_location VARCHAR,
    i_management_ip VARCHAR,
    i_operational_state VARCHAR,
    i_organisation VARCHAR,
    i_poll_interval VARCHAR,
    i_rack_position VARCHAR,
    i_relation VARCHAR,
    i_serial_number VARCHAR,
    i_usage_state VARCHAR,
    i_building_name VARCHAR,
    i_access_key VARCHAR,
    i_order_id BIGINT,
    i_id BIGINT,
    inOUT success INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Update data in the device table
    UPDATE device 
    SET 
        administrative_state = i_administrative_state,
        credentials = i_credentials,
        customer = i_customer,
        device_model = i_device_model,
        devicename = i_devicename,
        href = i_href,
        location = i_location,
        management_ip = i_management_ip,
        operational_state = i_operational_state,
        organisation = i_organisation,
        poll_interval = i_poll_interval,
        rack_position = i_rack_position,
        realtion = i_relation,
        serial_number = i_serial_number,
        usage_state = i_usage_state,
        building_name = i_building_name,
        access_key = i_access_key,
        order_id = i_order_id
    WHERE 
        id = i_id;

    -- Set success to 1 indicating successful execution
    success := 1;
    
EXCEPTION
    -- If an error occurs, set success to 0
    WHEN OTHERS THEN
        success := 0;
END;
$$;


---------------------------------       end -------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION update_shelf_name()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE shelf
    SET name = REPLACE(name, OLD.devicename || '_shelf_', NEW.devicename || '_shelf_')
    WHERE name LIKE OLD.devicename || '_shelf_%';
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER device_update_shelf_name
AFTER UPDATE ON device
FOR EACH ROW
EXECUTE FUNCTION update_shelf_name();

-------------------------------------  end ----------------------------------------------------------------------------------------------


SELECT proname
FROM pg_proc
JOIN pg_namespace ON pg_proc.pronamespace = pg_namespace.oid
JOIN pg_class ON pg_proc.proargtypes[0] = pg_class.oid
WHERE pg_namespace.nspname = 'public'
AND pg_class.relname = 'shelf'
AND prokind = 'f';

-------------------------------------- end ---------------------------------------------------------------------------------------------

SELECT constraint_name, constraint_type
FROM information_schema.table_constraints
WHERE table_schema = 'public' -- Replace 'public' with your schema name if different
  AND table_name = 'state'; -- Replace 'your_table_name' with the name of your TABLE
  
  SELECT
    tc.constraint_name,
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name,
    tc.constraint_type
FROM
    information_schema.table_constraints AS tc
    JOIN information_schema.key_column_usage AS kcu
      ON tc.constraint_name = kcu.constraint_name
    JOIN information_schema.constraint_column_usage AS ccu
      ON ccu.constraint_name = tc.constraint_name
WHERE
    tc.table_name = 'state';
	
	
 ALTER TABLE device  
ADD CONSTRAINT deviceorder
FOREIGN KEY (order_id) 
REFERENCES orders(id)
ON UPDATE CASCADE
ON DELETE CASCADE;
	
-------------------------------- end ---------------------------------------------------------------------------------------------------------


CREATE OR REPLACE PROCEDURE insert_card(
    i_cardname VARCHAR,
    i_devicename VARCHAR,
    i_shelfPosition INTEGER,
    i_slotPosition INTEGER,
    i_vendor VARCHAR,
    i_cardModel VARCHAR,
    i_cardPartNumber VARCHAR,
    i_operationalState VARCHAR,
    i_administrativeState VARCHAR,
    i_usageState VARCHAR,
    i_href VARCHAR,
    i_orderid INTEGER,
    inout success INT
) LANGUAGE plpgsql
AS $$
DECLARE
    shelfs VARCHAR(100); -- Declare shelfs variable
BEGIN
    -- Retrieve shelf name based on device name and shelf position
    SELECT name INTO shelfs
    FROM shelf
    WHERE devicename = i_devicename AND shelf_position = i_shelfPosition;

    -- Insert data into Slot table
    INSERT INTO slot (
        slotname,
        slot_position,
        operational_state,
        administrative_state,
        usage_state,
        href,
        shelfname,
        relation
    ) VALUES (
        i_devicename || '/' || i_shelfPosition || '/' || i_slotPosition, -- Generate slotname in desired format
        i_slotPosition,
        i_operationalState,
        i_administrativeState,
        i_usageState,
        i_href,
        shelfs,
        'shelf_to_slot'
    );

    -- Insert data into Card table
    INSERT INTO card (
        cardname,
        devicename,
        slotname,
        shelf_position,
        slot_position,
        vendor,
        card_model,
        card_part_number,
        operational_state,
        administrative_state,
        usage_state,
        href,
        order_id,
        realation
    ) VALUES (
        i_cardname,
        i_devicename,
        i_devicename || '/' || i_shelfPosition || '/' || i_slotPosition, -- Generate slotname in desired format
        i_shelfPosition,
        i_slotPosition,
        i_vendor,
        i_cardModel,
        i_cardPartNumber,
        i_operationalState,
        i_administrativeState,
        i_usageState,
        i_href,
        i_orderid,
        'slot_to_card'
    );

    -- Set success flag to 1 indicating successful execution
    success := 1;

EXCEPTION
    WHEN OTHERS THEN
        -- Set success flag to 0 indicating failure
        success := 0;

        -- Rollback the transaction
        ROLLBACK;

        -- Raise the exception
        RAISE;
END;
$$;



----------------------------------- end -----------------------------------------------------------------------------------------------


CREATE OR REPLACE PROCEDURE update_card(
    i_cardid BIGINT,
    i_cardname VARCHAR,
    i_devicename VARCHAR,
    i_shelfPosition INTEGER,
    i_slotPosition INTEGER,
    i_vendor VARCHAR,
    i_cardModel VARCHAR,
    i_cardPartNumber VARCHAR,
    i_operationalState VARCHAR,
    i_administrativeState VARCHAR,
    i_usageState VARCHAR,
    i_href VARCHAR,
    i_orderid BIGINT,
    i_slotid BIGINT, -- Added slotid parameter
    inout success INT
) 
LANGUAGE plpgsql
AS $$
DECLARE
    shelf_name VARCHAR(100); -- Declare shelf_name variable
BEGIN
    -- Retrieve shelf name based on device name and shelf position
    SELECT name INTO shelf_name
    FROM shelf
    WHERE devicename = i_devicename AND shelf_position = i_shelfPosition;

    -- Update data in Slot table
    UPDATE slot
    SET
        slot_position = i_slotPosition,
        operational_state = i_operationalState,
        administrative_state = i_administrativeState,
        usage_state = i_usageState,
        href = i_href,
        shelfname = shelf_name, -- Update the shelf name
        slotname = i_devicename || '/' || i_shelfPosition || '/' || i_slotPosition -- Set the slot name based on slot id
    WHERE
        id = i_slotid; -- Update slot information based on slot id

    -- Update data in Card table
    UPDATE card
    SET
        cardname = i_cardname,
        devicename = i_devicename,
        slotname = i_devicename || '/' || i_shelfPosition || '/' || i_slotPosition, -- Set the slot name based on slot id
        shelf_position = i_shelfPosition,
        slot_position = i_slotPosition,
        vendor = i_vendor,
        card_model = i_cardModel,
        card_part_number = i_cardPartNumber,
        operational_state = i_operationalState,
        administrative_state = i_administrativeState,
        usage_state = i_usageState,
        href = i_href,
        order_id = i_orderid
    WHERE
        cardid = i_cardid;

    -- Set success flag to 1 indicating successful execution
    success := 1;

EXCEPTION
    WHEN OTHERS THEN
        -- Set success flag to 0 indicating failure
        success := 0;

        -- Rollback the transaction
        ROLLBACK;

        -- Raise the exception
        RAISE;
END;
$$;

--------------------------------- end------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE insert_port(
    i_portname VARCHAR,
    i_positionOnCard INTEGER,
    i_positionOnDevice INTEGER,
    i_operationalState VARCHAR,
    i_administrativeState VARCHAR,
    i_usageState VARCHAR,
    i_href VARCHAR,
    i_PortSpeed VARCHAR,
    i_Capacity INTEGER,
    i_managementIp VARCHAR,
    i_relation VARCHAR,
    i_cardname VARCHAR,
    i_cardSlotName VARCHAR,
    i_order_id BIGINT,
    i_devicename VARCHAR,
    i_cardid BIGINT,
    inout o_success INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Create or get card slot
    INSERT INTO card_slot (name, slot_position, operational_state, administrative_state, usage_state, href, cardname, realation, cardid)
    VALUES (i_cardSlotName, i_positionOnCard, i_operationalState, i_administrativeState, i_usageState, i_href, i_cardname, 'card-to-cardslot', i_cardid);

    -- Create port
    INSERT INTO port (portname, position_on_card, position_on_device, port_type, operational_state, administrative_state, usage_state, href, port_speed, capacity, management_ip, relation, cardname, cardlslotname, order_id, devicename)
    VALUES (i_portname, i_positionOnCard, 0, 'card-to-port', i_operationalState, i_administrativeState, i_usageState, i_href, i_PortSpeed, i_Capacity, i_managementIp, 'Cardslot-to-port', i_cardname, i_cardSlotName, i_order_id, i_devicename);
    
    o_success := 1; -- Success
EXCEPTION
    WHEN others THEN
        -- If an exception occurs, rollback the transaction
        ROLLBACK;
        o_success := 0; -- Failure
        RAISE;
END;
$$;


--------------------------------- end------------------------------------------------------------------------------------


CREATE OR REPLACE PROCEDURE insert_deviceport(
    i_portname VARCHAR,
    i_positionOnDevice INTEGER,
    i_operationalState VARCHAR,
    i_administrativeState VARCHAR,
    i_usageState VARCHAR,
    i_href VARCHAR,
    i_PortSpeed VARCHAR,
    i_Capacity INTEGER,
    i_managementIp VARCHAR,
    i_relation VARCHAR,
    i_order_id BIGINT,
    i_devicename VARCHAR,
    inout o_success INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Start a transaction
    BEGIN
        -- Create port
        INSERT INTO port (portname, position_on_card, position_on_device, port_type, operational_state, administrative_state, usage_state, href, port_speed, capacity, management_ip, relation, cardname, cardlslotname, order_id, devicename)
        VALUES (i_portname, 0, i_positionOnDevice, 'device-to-port', i_operationalState, i_administrativeState, i_usageState, i_href, i_PortSpeed, i_Capacity, i_managementIp, 'device-to-port', null, null, i_order_id, i_devicename);
        
        -- If no exceptions, commit the transaction
        o_success := 1; -- Success
    EXCEPTION
        WHEN others THEN
            -- If an exception occurs, rollback the transaction
            ROLLBACK;
            o_success := 0; -- Failure
            RAISE;
    END;
END;
$$;
--- end------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE insert_pluggable(
    i_pluggablename VARCHAR,
    i_positionOnCard INTEGER,
    i_positionOnDevice INTEGER,
    i_vendor VARCHAR,
    i_pluggableModel VARCHAR,
    i_pluggablePartNumber VARCHAR,
    i_operationalState VARCHAR,
    i_administrativeState VARCHAR,
    i_usageState VARCHAR,
    i_href VARCHAR,
    i_managementIp VARCHAR,
    i_relation VARCHAR,
    i_cardname VARCHAR,
    i_cardSlotName VARCHAR,
    i_order_id BIGINT,
    i_devicename VARCHAR,
    i_cardid BIGINT,
    inout o_success INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Start a transaction
    BEGIN
        -- Create or get card slot
        INSERT INTO card_slot (name, slot_position, operational_state, administrative_state, usage_state, href, cardname, realation, cardid)
        VALUES (i_cardSlotName, i_positionOnCard, i_operationalState, i_administrativeState, i_usageState, i_href, i_cardname, 'card-to-cardslot', i_cardid);

        -- Create pluggable
        INSERT INTO pluggable (plugablename, position_on_card, position_on_device, vendor, pluggable_model, pluggable_part_number, operational_state, administrative_state, usage_state, href, management_ip, relation, cardname, cardlslotname, order_id, devicename)
        VALUES (i_pluggablename, i_positionOnCard, 0, i_vendor, i_pluggableModel, i_pluggablePartNumber, i_operationalState, i_administrativeState, i_usageState, i_href, i_managementIp, 'cardslot-to-pluggable', i_cardname, i_cardSlotName, i_order_id, i_devicename);

        -- If no exceptions, commit the transaction
        o_success := 1; -- Success
    EXCEPTION
        WHEN others THEN
            -- If an exception occurs, rollback the transaction
            ROLLBACK;
            o_success := 0; -- Failure
            RAISE;
    END;
END;
$$;

------------------- end------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION global_search(table_name TEXT, column_name TEXT, search_term TEXT, filter_type TEXT DEFAULT 'contains')
RETURNS JSON AS $$
DECLARE
    result JSON;
    filter_condition TEXT;
BEGIN
    -- Construct the filter condition based on the filter_type parameter
    CASE filter_type
        WHEN 'contains' THEN
            filter_condition := '%' || search_term || '%';
        WHEN 'startwith' THEN
            filter_condition := search_term || '%';
        WHEN 'endwith' THEN
            filter_condition := '%' || search_term;
        ELSE
            RAISE EXCEPTION 'Invalid filter_type: %', filter_type;
    END CASE;

    -- Construct and execute the dynamic SQL query
    EXECUTE FORMAT('
        SELECT json_agg(t)
        FROM (
            SELECT *
            FROM %I
            WHERE CASE
                WHEN $1 ~ ''^\d+$'' THEN %I::INTEGER = $1::INTEGER
                ELSE CAST(%I AS TEXT) LIKE $2
            END
        ) t', table_name, column_name, column_name)
    INTO result
    USING search_term, filter_condition; -- Pass filter_condition directly
    
    RETURN result;
END;
$$ LANGUAGE plpgsql;



------------------------- end------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE insert_pluggabledevice(
    i_pluggablename VARCHAR,
    i_positionOnCard INTEGER,
    i_positionOnDevice INTEGER,
    i_vendor VARCHAR,
    i_pluggableModel VARCHAR,
    i_pluggablePartNumber VARCHAR,
    i_operationalState VARCHAR,
    i_administrativeState VARCHAR,
    i_usageState VARCHAR,
    i_href VARCHAR,
    i_managementIp VARCHAR,
    i_order_id BIGINT,
    i_devicename VARCHAR,
    inout o_success INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Start a transaction
    BEGIN
        -- Create pluggable
        INSERT INTO pluggable (plugablename, position_on_card, position_on_device, vendor, pluggable_model, pluggable_part_number, operational_state, administrative_state, usage_state, href, management_ip, relation, cardname, cardlslotname, order_id, devicename)
        VALUES (i_pluggablename, 0, i_positionOnDevice, i_vendor, i_pluggableModel, i_pluggablePartNumber, i_operationalState, i_administrativeState, i_usageState, i_href, i_managementIp, 'device-to-pluggable', null, null, i_order_id, i_devicename);

        -- If no exceptions, commit the transaction
        o_success := 1; -- Success
    EXCEPTION
        WHEN others THEN
            -- If an exception occurs, rollback the transaction
            ROLLBACK;
            o_success := 0; -- Failure
            RAISE;
    END;
END;
$$;

-----------------------end------------------------------------------------------------------------------------











