
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
    keys VARCHAR[],
    p_values VARCHAR[],
    INOUT success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    d_inserted_id BIGINT;
    shelf_count INT;
    shelf_position INT;
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;

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
        RETURNING id INTO d_inserted_id;
        -- Check if both keys and values arrays have lengths greater than 0
        IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
            -- Iterate over each key-value pair and insert into the additional_attribute table
            FOR i IN 1..array_length(keys, 1) LOOP
                -- Insert into additional_attribute table and retrieve the ID
                INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                -- Append the inserted ID to the inserted_ids array
                inserted_ids := inserted_ids || ARRAY[inserted_id];

                -- Insert into physicalconnection_additional_attribute table
                INSERT INTO device_additional_attribute (device_id, additional_attribute_id)
                VALUES (d_inserted_id, inserted_id);
            END LOOP;
        END IF;


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
                    'DEVICE_TO_SHELF'
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
    keys VARCHAR[],
    p_values VARCHAR[],
    INOUT success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    inserted_id BIGINT;
    existing_id BIGINT;
    inserted_ids BIGINT[] := '{}';
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

    -- Iterate over each key-value pair and insert or update additional attributes
    FOR i IN 1..array_length(keys, 1) LOOP
        -- Check if the key-value pair already exists for this device
        BEGIN
            SELECT id INTO existing_id
            FROM additional_attribute aa
            JOIN device_additional_attribute daa ON aa.id = daa.additional_attribute_id
            WHERE daa.device_id = i_id
            AND aa."key" = keys[i]
            AND aa."value" = p_values[i];

            IF existing_id IS NOT NULL THEN
                UPDATE device_additional_attribute
                SET device_id = i_id
                WHERE device_id = i_id AND additional_attribute_id = existing_id;
            ELSE
                -- Key-value pair does not exist, insert new additional attribute
                INSERT INTO additional_attribute ("key", "value")
                VALUES (keys[i], p_values[i])
                RETURNING id INTO inserted_id;

                -- Insert into device_additional_attribute table
                INSERT INTO device_additional_attribute (device_id, additional_attribute_id)
                VALUES (i_id, inserted_id);
            END IF;
        EXCEPTION
            WHEN OTHERS THEN
                -- Log or handle the specific error here
                RAISE NOTICE 'Error processing key-value pair: %, %', keys[i], p_values[i];
        END;
    END LOOP;

    -- Set success to 1 indicating successful execution
    success := 1;

EXCEPTION
    -- If an error occurs, set success to 0
    WHEN OTHERS THEN
        success := 0;
        -- Log or handle the general error here
        RAISE NOTICE 'Error updating device: %', SQLERRM;
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
    i_orderid BIGINT,
    keys VARCHAR[],
    p_values VARCHAR[],
    inout success INT
) LANGUAGE plpgsql
AS $$
DECLARE
    shelfs VARCHAR(100); -- Declare shelfs variable
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;
BEGIN
    -- Retrieve shelf name based on device name and shelf position
    BEGIN
        SELECT name INTO shelfs
        FROM shelf
        WHERE devicename = i_devicename AND shelf_position = i_shelfPosition;
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error retrieving shelf name for device: %, shelf position: %. Error: %', i_devicename, i_shelfPosition, SQLERRM;
    END;

    -- Insert data into Slot table
    BEGIN
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
            'SHELF_TO_SLOT'
        );
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting into Slot table for device: %, shelf position: %, slot position: %. Error: %', i_devicename, i_shelfPosition, i_slotPosition, SQLERRM;
    END;

    -- Insert data into Card table
    BEGIN
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
            'SLOT_TO_CARD'
        );
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting into Card table for card: %, device: %, shelf position: %, slot position: %. Error: %', i_cardname, i_devicename, i_shelfPosition, i_slotPosition, SQLERRM;
    END;

    -- Insert additional attributes
    BEGIN
        -- Check if both keys and values arrays have lengths greater than 0
        IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
            -- Iterate over each key-value pair and insert into the additional_attribute table
            FOR i IN 1..array_length(keys, 1) LOOP
                -- Insert into additional_attribute table and retrieve the ID
                INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                -- Append the inserted ID to the inserted_ids array
                inserted_ids := inserted_ids || ARRAY[inserted_id];

                -- Insert into physicalconnection_additional_attribute table
                INSERT INTO card_additional_attribute (cardname, devicename, additional_attribute_id)
                VALUES (i_cardname, i_devicename, inserted_id);
            END LOOP;
        END IF;
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting additional attributes for card: %, device: %. Error: %', i_cardname, i_devicename, SQLERRM;
    END;

    -- Set success flag to 1 indicating successful execution
    success := 1;

EXCEPTION
    WHEN OTHERS THEN
        -- Set success flag to 0 indicating failure
        success := 0;
        -- Rollback the transaction
        ROLLBACK;
        -- Raise the exception with a custom message
        RAISE EXCEPTION 'Error in procedure insert_card. Error: %', SQLERRM;
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
    keys VARCHAR[],
    p_values VARCHAR[],
    INOUT success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    shelf_name VARCHAR(100); -- Declare shelf_name variable
    existing_id BIGINT;
    inserted_id BIGINT;
BEGIN
    BEGIN
        -- Retrieve shelf name based on device name and shelf position
        SELECT name INTO shelf_name
        FROM shelf
        WHERE devicename = i_devicename AND shelf_position = i_shelfPosition;
        RAISE NOTICE 'Shelf name retrieved: %', shelf_name;
    EXCEPTION
        WHEN OTHERS THEN
            success := 0;
            RAISE NOTICE 'Error retrieving shelf name: % %', SQLERRM, SQLSTATE;
            RAISE;
    END;

    BEGIN
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
        RAISE NOTICE 'Slot table updated for slot id: %', i_slotid;
    EXCEPTION
        WHEN OTHERS THEN
            success := 0;
            RAISE NOTICE 'Error updating slot table: % %', SQLERRM, SQLSTATE;
            RAISE;
    END;

    BEGIN
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
        RAISE NOTICE 'Card table updated for card id: %', i_cardid;
    EXCEPTION
        WHEN OTHERS THEN
            success := 0;
            RAISE NOTICE 'Error updating card table: % %', SQLERRM, SQLSTATE;
            RAISE;
    END;

    FOR i IN 1..array_length(keys, 1) LOOP
        BEGIN
            -- Check if the key-value pair already exists for this device
            SELECT id INTO existing_id
            FROM additional_attribute aa
            JOIN card_additional_attribute daa ON aa.id = daa.additional_attribute_id
            WHERE daa.devicename = i_devicename
            AND daa.cardname = i_cardname
            AND aa."key" = keys[i]
            AND aa."value" = p_values[i];
            RAISE NOTICE 'Checked existence for key-value pair: %, %', keys[i], p_values[i];

            IF existing_id IS NOT NULL THEN
                BEGIN
                    UPDATE card_additional_attribute
                    SET cardname = i_cardname, devicename = i_devicename
                    WHERE devicename = i_devicename AND cardname = i_cardname AND additional_attribute_id = existing_id;
                    RAISE NOTICE 'Updated additional attribute: %, %', keys[i], p_values[i];
                EXCEPTION
                    WHEN OTHERS THEN
                        success := 0;
                        RAISE NOTICE 'Error updating additional attribute: %, % % %', keys[i], p_values[i], SQLERRM, SQLSTATE;
                        RAISE;
                END;
            ELSE
                BEGIN
                    -- Key-value pair does not exist, insert new additional attribute
                    INSERT INTO additional_attribute ("key", "value")
                    VALUES (keys[i], p_values[i])
                    RETURNING id INTO inserted_id;
                    RAISE NOTICE 'Inserted new additional attribute: %, %', keys[i], p_values[i];
                EXCEPTION
                    WHEN OTHERS THEN
                        success := 0;
                        RAISE NOTICE 'Error inserting additional attribute: %, % % %', keys[i], p_values[i], SQLERRM, SQLSTATE;
                        RAISE;
                END;

                BEGIN
                    -- Insert into device_additional_attribute table
                    INSERT INTO card_additional_attribute (devicename, cardname, additional_attribute_id)
                    VALUES (i_devicename, i_cardname, inserted_id);
                    RAISE NOTICE 'Inserted into card_additional_attribute for: %, %', i_devicename, i_cardname;
                EXCEPTION
                    WHEN OTHERS THEN
                        success := 0;
                        RAISE NOTICE 'Error inserting into card_additional_attribute: % %', SQLERRM, SQLSTATE;
                        RAISE;
                END;
            END IF;
        EXCEPTION
            WHEN OTHERS THEN
                success := 0;
                RAISE NOTICE 'Error processing key-value pair: %, % % %', keys[i], p_values[i], SQLERRM, SQLSTATE;
                RAISE;
        END;
    END LOOP;

    -- Set success flag to 1 indicating successful execution
    success := 1;
    RAISE NOTICE 'Procedure executed successfully';
EXCEPTION
    WHEN OTHERS THEN
        -- Rollback the transaction in case of any error
        RAISE NOTICE 'Error in procedure: % %', SQLERRM, SQLSTATE;
        ROLLBACK;
        RAISE;
END;
$$;

--------------------------------- end-----------------------------------------------------------------------------------

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
    keys VARCHAR[],
    p_values VARCHAR[],
    inout o_success INT
)
LANGUAGE plpgsql
AS $$
declare
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;
    d_inserted_id BIGINT;
BEGIN
    -- Create or get card slot
    BEGIN
        INSERT INTO card_slot (name, slot_position, operational_state, administrative_state, usage_state, href, cardname, realation, cardid)
        VALUES (i_cardSlotName, i_positionOnCard, i_operationalState, i_administrativeState, i_usageState, i_href, i_cardname, 'CARD-TO-CARDSLOT', i_cardid);
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting into Card Slot table for Port: %, slot position: %. Error: %', i_cardname, i_positionOnCard, SQLERRM;
    END;

    -- Create port
    BEGIN
        INSERT INTO port (portname, position_on_card, position_on_device, port_type, operational_state, administrative_state, usage_state, href, port_speed, capacity, management_ip, relation, cardname, cardlslotname, order_id, devicename)
        VALUES (i_portname, i_positionOnCard, 0, 'card-to-port', i_operationalState, i_administrativeState, i_usageState, i_href, i_PortSpeed, i_Capacity, i_managementIp, 'CARDSLOT_TO_PORT', i_cardname, i_cardSlotName, i_order_id, i_devicename)
        RETURNING portid INTO d_inserted_id;
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting into Port table for CARD: %, portname: %. Error: %', i_cardname, i_portname, SQLERRM;
    END;

     -- Insert additional attributes
    BEGIN
        -- Check if both keys and values arrays have lengths greater than 0
        IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
            -- Iterate over each key-value pair and insert into the additional_attribute table
            FOR i IN 1..array_length(keys, 1) LOOP
                -- Insert into additional_attribute table and retrieve the ID
                INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                -- Append the inserted ID to the inserted_ids array
                inserted_ids := inserted_ids || ARRAY[inserted_id];

                -- Insert into physicalconnection_additional_attribute table
                INSERT INTO port_additional_attribute (port_id, additional_attribute_id)
                VALUES (d_inserted_id,inserted_id);
            END LOOP;
        END IF;
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting additional attributes for port: %. Error: %', i_portname, SQLERRM;
    END;

    o_success := 1; -- Success
EXCEPTION
    WHEN others THEN
        -- If an exception occurs, rollback the transaction
        ROLLBACK;
        o_success := 0; -- Failure
        RAISE;
END;
$$


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
    keys VARCHAR[],
    p_values VARCHAR[],
    inout o_success INT
)
LANGUAGE plpgsql
AS $$
declare
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;
    d_inserted_id BIGINT;
BEGIN
    -- Start a transaction
    BEGIN
        -- Create port
        INSERT INTO port (portname, position_on_card, position_on_device, port_type, operational_state, administrative_state, usage_state, href, port_speed, capacity, management_ip, relation, cardname, cardlslotname, order_id, devicename)
        VALUES (i_portname, 0, i_positionOnDevice, 'DEVICE-TO-PORT', i_operationalState, i_administrativeState, i_usageState, i_href, i_PortSpeed, i_Capacity, i_managementIp, i_relation, null, null, i_order_id, i_devicename)
         RETURNING portid INTO d_inserted_id;

    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting into Port table for DEVICE: %, portname: %. Error: %', i_devicename, i_portname, SQLERRM;
    END;

    -- Insert additional attributes
    BEGIN
        -- Check if both keys and values arrays have lengths greater than 0
        IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
            -- Iterate over each key-value pair and insert into the additional_attribute table
            FOR i IN 1..array_length(keys, 1) LOOP
                -- Insert into additional_attribute table and retrieve the ID
                INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                -- Append the inserted ID to the inserted_ids array
                inserted_ids := inserted_ids || ARRAY[inserted_id];

                -- Insert into port_additional_attribute table
                INSERT INTO port_additional_attribute (port_id, additional_attribute_id)
                VALUES (d_inserted_id, inserted_id);
            END LOOP;
        END IF;
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting additional attributes for port: %. Error: %', i_portname, SQLERRM;
    END;

    -- If no exceptions, commit the transaction
    o_success := 1; -- Success

EXCEPTION
    WHEN others THEN
        -- If an exception occurs, rollback the transaction
        ROLLBACK;
        o_success := 0; -- Failure
        RAISE;
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
    keys VARCHAR[],
    p_values VARCHAR[],
    inout o_success INT
)
LANGUAGE plpgsql
AS $$
declare
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;
    d_inserted_id BIGINT;
BEGIN
    -- Create or get card slot
    BEGIN
        INSERT INTO card_slot (name, slot_position, operational_state, administrative_state, usage_state, href, cardname, realation, cardid)
        VALUES (i_cardSlotName, i_positionOnCard, i_operationalState, i_administrativeState, i_usageState, i_href, i_cardname, 'CARD-TO-CARDSLOT', i_cardid);
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting into card_slot table for card: %, slot position: %. Error: %', i_cardname, i_positionOnCard, SQLERRM;
    END;

    -- Create pluggable
    BEGIN
        INSERT INTO pluggable (plugablename, position_on_card, position_on_device, vendor, pluggable_model, pluggable_part_number, operational_state, administrative_state, usage_state, href, management_ip, relation, cardname, cardlslotname, order_id, devicename)
        VALUES (i_pluggablename, i_positionOnCard, 0, i_vendor, i_pluggableModel, i_pluggablePartNumber, i_operationalState, i_administrativeState, i_usageState, i_href, i_managementIp, 'CARDSLOT-TO-PLUGGABLE', i_cardname, i_cardSlotName, i_order_id, i_devicename)
        RETURNING id INTO d_inserted_id;
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting into pluggable table for device: %, pluggable name: %. Error: %', i_devicename, i_pluggablename, SQLERRM;
    END;

    -- Insert additional attributes
    BEGIN
        -- Check if both keys and values arrays have lengths greater than 0
        IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
            -- Iterate over each key-value pair and insert into the additional_attribute table
            FOR i IN 1..array_length(keys, 1) LOOP
                -- Insert into additional_attribute table and retrieve the ID
                INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                -- Append the inserted ID to the inserted_ids array
                inserted_ids := inserted_ids || ARRAY[inserted_id];

                -- Insert into pluggable_additional_attribute table
                INSERT INTO pluggable_additional_attribute (pluggable_id, additional_attribute_id)
                VALUES (d_inserted_id, inserted_id);
            END LOOP;
        END IF;
    EXCEPTION WHEN OTHERS THEN
        RAISE EXCEPTION 'Error inserting additional attributes for pluggable: %. Error: %', i_pluggablename, SQLERRM;
    END;

    -- If no exceptions, commit the transaction
    o_success := 1; -- Success

EXCEPTION
    WHEN others THEN
        -- If an exception occurs, rollback the transaction
        ROLLBACK;
        o_success := 0; -- Failure
        RAISE;
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
    keys VARCHAR[],
    p_values VARCHAR[],
    INOUT o_success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;
    d_inserted_id BIGINT;
BEGIN
    -- Start a transaction
    BEGIN
        BEGIN
            -- Create pluggable
            INSERT INTO pluggable (plugablename, position_on_card, position_on_device, vendor, pluggable_model, pluggable_part_number, operational_state, administrative_state, usage_state, href, management_ip, relation, cardname, cardlslotname, order_id, devicename)
            VALUES (i_pluggablename, 0, i_positionOnDevice, i_vendor, i_pluggableModel, i_pluggablePartNumber, i_operationalState, i_administrativeState, i_usageState, i_href, i_managementIp, 'DEVICE-TO-PLUGGABLE', NULL, NULL, i_order_id, i_devicename)
            RETURNING id INTO d_inserted_id;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Error inserting into Pluggable table for DEVICE: %, pluggablename: %. Error: %', i_devicename, i_pluggablename, SQLERRM;
        END;

        BEGIN
            -- Insert additional attributes
            IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
                FOR i IN 1..array_length(keys, 1) LOOP
                    BEGIN
                        -- Insert into additional_attribute table and retrieve the ID
                        INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                        -- Append the inserted ID to the inserted_ids array
                        inserted_ids := inserted_ids || ARRAY[inserted_id];

                        -- Insert into pluggable_additional_attribute table
                        INSERT INTO pluggable_additional_attribute (pluggable_id, additional_attribute_id)
                        VALUES (d_inserted_id, inserted_id);
                    EXCEPTION
                        WHEN OTHERS THEN
                            RAISE EXCEPTION 'Error inserting additional attribute key: %, value: %. Error: %', keys[i], p_values[i], SQLERRM;
                    END;
                END LOOP;
            END IF;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Error inserting additional attributes for pluggable: %. Error: %', i_pluggablename, SQLERRM;
        END;

        o_success := 1; -- Success
    EXCEPTION
        WHEN OTHERS THEN
            -- If an exception occurs, rollback the transaction
            o_success := 0; -- Failure
            RAISE;
    END;
END;
$$;

-----------------------end------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE update_port_on_device(
    IN p_portid BIGINT,
    IN p_portname VARCHAR(255),
    IN p_positionOnDevice INTEGER,
    IN p_portType VARCHAR(255),
    IN p_operationalState VARCHAR(255),
    IN p_administrativeState VARCHAR(255),
    IN p_usageState VARCHAR(255),
    IN p_href VARCHAR(255),
    IN p_portSpeed VARCHAR(255),
    IN p_capacity INTEGER,
    IN p_managementIp VARCHAR(255),
    IN p_orderId BIGINT,
    IN p_deviceName VARCHAR(255),
    INOUT success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    existcardslotname VARCHAR(255);
BEGIN
    -- Initialize success flag
    success := 0;
    -- Begin transaction
    BEGIN       
        -- Check if data exists at the specified position
      IF EXISTS (
    SELECT 1 
    FROM port 
    WHERE devicename = p_deviceName AND position_on_device = p_positionOnDevice
) THEN
    -- If data exists, raise an exception
    RAISE EXCEPTION 'Port device % already exists at the specified position on device %', p_deviceName, p_positionOnDevice;
END IF;

IF EXISTS (
    SELECT 1 
    FROM pluggable 
    WHERE devicename = p_deviceName AND position_on_device = p_positionOnDevice
) THEN
    -- If data exists, raise an exception
    RAISE EXCEPTION 'Pluggable device % already exists at the specified position on device %', p_deviceName, p_positionOnDevice;
END IF;

        
        -- Get the cardslotname associated with the portid
        SELECT cardlslotname INTO existcardslotname FROM port WHERE portid = p_portid;

        -- Perform an update in the port table
        UPDATE port
        SET portname = p_portname,
            position_on_card = 0,
            position_on_device = p_positionOnDevice,
            port_type = 'device-to-port',
            operational_state = p_operationalState,
            administrative_state = p_administrativeState,
            usage_state = p_usageState,
            href = p_href,
            port_speed = p_portSpeed,
            capacity = p_capacity,
            management_ip = p_managementIp,
            relation = 'device-to-port',
            cardname = null,
            cardlslotname = null,  -- Set cardslotname to null
            order_id = p_orderId,
            devicename = p_deviceName
        WHERE portid = p_portid;
        
        -- If the retrieved cardslotname exists, delete the corresponding record from the card_slot table
        IF existcardslotname IS NOT NULL THEN
            DELETE FROM card_slot WHERE name = existcardslotname;
        END IF;
        
        success := 1; -- Set success flag to 1 if the update is successful

        
    EXCEPTION
        WHEN OTHERS THEN
            -- If any exception occurs, rollback transaction and set success flag to 0
            ROLLBACK;
            success := 0;
            -- Re-raise the exception for handling at the calling end
            RAISE;
    END;
END;
$$;
--------------- end------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE update_port_on_card(
    IN p_portid BIGINT,
    IN p_portname VARCHAR(255),
    IN p_positionOnCard INTEGER,
    IN p_portType VARCHAR(255),
    IN p_operationalState VARCHAR(255),
    IN p_administrativeState VARCHAR(255),
    IN p_usageState VARCHAR(255),
    IN p_href VARCHAR(255),
    IN p_portSpeed VARCHAR(255),
    IN p_cardname VARCHAR(255),
    IN p_cardslotname VARCHAR(255),
    IN p_capacity INTEGER,
    IN p_managementIp VARCHAR(255),
    IN p_orderId BIGINT,
    IN p_deviceName VARCHAR(255),
    INOUT success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    existcardslotname VARCHAR(255);
    d_cardid BIGINT;
    d_cardslotname VARCHAR(255);
BEGIN
    -- Initialize success flag
    success := 0;
    -- Begin transaction
    BEGIN
        -- Get the cardslotname associated with the portid
        SELECT cardlslotname INTO existcardslotname FROM port WHERE portid = p_portid;

        -- Check if cardid exists for the provided cardname and devicename
        SELECT cardid INTO d_cardid FROM card WHERE cardname = p_cardname AND devicename = p_deviceName;
        IF NOT FOUND THEN
            -- If cardid not found, raise an exception
            RAISE EXCEPTION 'Cardid not found for cardname % and devicename %', p_cardname, p_deviceName;
        END IF;

        -- Construct the full card slot name
        d_cardslotname := p_cardname || d_cardid || '/' || p_positionOnCard;

        -- Check if the retrieved cardslotname exists and is different from the input cardslotname
       
        IF existcardslotname IS NULL THEN
            -- If the cardslotname does not exist, insert a new record into the card_slot table
            INSERT INTO card_slot("name", slot_position, operational_state, administrative_state, usage_state, href, cardname, realation, cardid)
            VALUES (d_cardslotname, p_positionOnCard, p_operationalState, p_administrativeState, p_usageState, p_href, p_cardname, 'card-to-cardslot', d_cardid);
        ELSE
            -- If the cardslotname exists, check if it's different from the generated cardslotname
            IF existcardslotname != d_cardslotname THEN
                -- Check if the generated cardslotname already exists in the card_slot table
                PERFORM * FROM card_slot WHERE "name" = p_cardslotname;
                -- If a record exists, throw an exception indicating the cardslot is already occupied
                IF FOUND THEN
                    RAISE EXCEPTION 'Given cardslot already occupied';
                END IF;
            END IF;

            -- Update the corresponding record in the card_slot table
            UPDATE card_slot
            SET "name" = p_cardslotname,
                slot_position = p_positionOnCard,
                operational_state = p_operationalState,
                administrative_state = p_administrativeState,
                usage_state = p_usageState,
                href = p_href,
                cardname = p_cardname,
                realation = 'card-to-port', -- Corrected the column name
                cardid = d_cardid
            WHERE "name" = existcardslotname;
        END IF;

        -- Perform an update in the port table
        UPDATE port
        SET portname = p_portname,
            position_on_card = p_positionOnCard,
            position_on_device = 0,
            port_type = 'card-to-port',
            operational_state = p_operationalState,
            administrative_state = p_administrativeState,
            usage_state = p_usageState,
            href = p_href,
            port_speed = p_portSpeed,
            capacity = p_capacity,
            management_ip = p_managementIp,
            relation = 'cardslot-to-port',
            cardname = p_cardname,
            cardlslotname = p_cardslotname,
            order_id = p_orderId,
            devicename = NULL -- Changed 'null' to NULL
        WHERE portid = p_portid;
        
        -- Set success flag to 1 if the update is successful
        success := 1;
        
    EXCEPTION
        WHEN OTHERS THEN
            -- If any exception occurs, rollback transaction and set success flag to 0
            ROLLBACK;
            success := 0;
            -- Re-raise the exception for handling at the calling end
            RAISE;
    END;
END;
$$;
-------------------end------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE update_pluggable_on_device(
    IN p_pluggableid BIGINT,
    IN p_pluggablename VARCHAR(255),
    IN p_pluggableModel VARCHAR(255),
    IN p_pluggablePartNumber VARCHAR(255),
    IN p_positionOnDevice INTEGER,
    IN p_operationalState VARCHAR(255),
    IN p_administrativeState VARCHAR(255),
    IN p_usageState VARCHAR(255),
    IN p_href VARCHAR(255),
    IN p_managementIp VARCHAR(255),
    IN p_vendor VARCHAR(255),
    IN p_orderId BIGINT,
    IN p_deviceName VARCHAR(255),
    INOUT success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    existcardlslotname VARCHAR(255);
BEGIN
    -- Initialize success flag
    success := 0;
    -- Begin transaction
    BEGIN       
        -- Check if data exists at the specified position
        IF EXISTS (
            SELECT 1 
            FROM pluggable 
            WHERE devicename = p_deviceName AND position_on_device = p_positionOnDevice
        ) THEN
            -- If data exists, raise an exception
            RAISE EXCEPTION 'Pluggable device % already exists at the specified position on device %', p_deviceName, p_positionOnDevice;
        END IF;

        IF EXISTS (
            SELECT 1 
            FROM port 
            WHERE devicename = p_deviceName AND position_on_device = p_positionOnDevice
        ) THEN
            -- If data exists, raise an exception
            RAISE EXCEPTION 'Port device % already exists at the specified position on device %', p_deviceName, p_positionOnDevice;
        END IF;

        -- Get the cardlslotname associated with the pluggableid
        SELECT cardlslotname INTO existcardlslotname FROM pluggable WHERE id = p_pluggableid;

        -- Perform an update in the pluggable table
        UPDATE pluggable
        SET plugablename = p_pluggablename,
            pluggable_model = p_pluggableModel,
            pluggable_part_number = p_pluggablePartNumber,
            position_on_device = p_positionOnDevice,
            operational_state = p_operationalState,
            administrative_state = p_administrativeState,
            usage_state = p_usageState,
            href = p_href,
            management_ip = p_managementIp,
            vendor = p_vendor,
            position_on_card = 0,
            cardname = null,
            cardlslotname = null,  -- Set cardlslotname to null
            order_id = p_orderId,
            relation='DEVICE-TO-PLUGGABLE',
            devicename = p_deviceName
        WHERE id = p_pluggableid;

        -- If the retrieved cardlslotname exists, delete the corresponding record from the card_slot table
        IF existcardlslotname IS NOT NULL THEN
            DELETE FROM card_slot WHERE name = existcardlslotname;
        END IF;

        success := 1; -- Set success flag to 1 if the update is successful

    EXCEPTION
        WHEN OTHERS THEN
            -- If any exception occurs, rollback transaction and set success flag to 0
            ROLLBACK;
            success := 0;
            -- Re-raise the exception for handling at the calling end
            RAISE;
    END;
END;
$$;
---------------------end------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE update_pluggable_on_card(
    IN p_pluggableid BIGINT,
    IN p_pluggablename VARCHAR(255),
    IN p_pluggableModel VARCHAR(255),
    IN p_pluggablePartNumber VARCHAR(255),
    IN p_positionOnCard INTEGER,
    IN p_operationalState VARCHAR(255),
    IN p_administrativeState VARCHAR(255),
    IN p_usageState VARCHAR(255),
    IN p_href VARCHAR(255),
    IN p_managementIp VARCHAR(255),
    IN p_vendor VARCHAR(255),
    IN p_orderId BIGINT,
    IN p_cardname VARCHAR(255),
    IN p_cardslotname VARCHAR(255),
    IN p_deviceName VARCHAR(255),
    INOUT success INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    existcardslotname VARCHAR(255);
    d_cardid BIGINT;
    d_cardslotname VARCHAR(255);
BEGIN
    -- Initialize success flag
    success := 0;
    -- Begin transaction
    BEGIN
        -- Get the cardslotname associated with the pluggableid
        SELECT cardlslotname INTO existcardslotname FROM pluggable WHERE id = p_pluggableid;

        -- Check if cardid exists for the provided cardname and devicename
        SELECT cardid INTO d_cardid FROM card WHERE cardname = p_cardname AND devicename = p_deviceName;
        IF NOT FOUND THEN
            -- If cardid not found, raise an exception
            RAISE EXCEPTION 'Cardid not found for cardname % and devicename %', p_cardname, p_deviceName;
        END IF;

        -- Construct the full card slot name
        d_cardslotname := p_cardname || d_cardid || '/' || p_positionOnCard;

        -- Check if the retrieved cardslotname exists and is different from the input cardslotname
        IF existcardslotname IS NULL THEN
            -- If the cardslotname does not exist, insert a new record into the card_slot table
            INSERT INTO card_slot("name", slot_position, operational_state, administrative_state, usage_state, href, cardname, realation, cardid)
            VALUES (d_cardslotname, p_positionOnCard, p_operationalState, p_administrativeState, p_usageState, p_href, p_cardname, 'card-to-cardslot', d_cardid);
        ELSE
            -- If the cardslotname exists, check if it's different from the generated cardslotname
            IF existcardslotname != d_cardslotname THEN
                -- Check if the generated cardslotname already exists in the card_slot table
                PERFORM * FROM card_slot WHERE "name" = p_cardslotname;
                -- If a record exists, throw an exception indicating the cardslot is already occupied
                IF FOUND THEN
                    RAISE EXCEPTION 'Given cardslot already occupied';
                END IF;
            END IF;

            -- Update the corresponding record in the card_slot table
            UPDATE card_slot
            SET "name" = p_cardslotname,
                slot_position = p_positionOnCard,
                operational_state = p_operationalState,
                administrative_state = p_administrativeState,
                usage_state = p_usageState,
                href = p_href,
                cardname = p_cardname,
                realation = 'card-to-pluggable', -- Corrected the column name
                cardid = d_cardid
            WHERE "name" = existcardslotname;
        END IF;

        -- Perform an update in the pluggable table
        UPDATE pluggable
        SET plugablename = p_pluggablename,
            pluggable_model = p_pluggableModel,
            pluggable_part_number = p_pluggablePartNumber,
            position_on_card = p_positionOnCard,
            position_on_device = 0,
            relation = 'CARDSLOT-TO-PLUGGABLE',
            operational_state = p_operationalState,
            administrative_state = p_administrativeState,
            usage_state = p_usageState,
            href = p_href,
            management_ip = p_managementIp,
            vendor = p_vendor,
            cardname = p_cardname,
            cardlslotname = p_cardslotname,
            order_id = p_orderId,
            devicename = devicename = NULL
        WHERE id = p_pluggableid;
        
        -- Set success flag to 1 if the update is successful
        success := 1;
        
    EXCEPTION
        WHEN OTHERS THEN
            -- If any exception occurs, rollback transaction and set success flag to 0
            ROLLBACK;
            success := 0;
            -- Re-raise the exception for handling at the calling end
            RAISE;
    END;
END;
$$;
------------------- end------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE insert_logical_portoncard(
    p_logicalportName VARCHAR,
    p_positionOnCard INTEGER,
    p_positionOnDevice INTEGER,
    p_portType VARCHAR,
    p_OperationalState VARCHAR,
    p_AdministrativeState VARCHAR,
    p_UsageState VARCHAR,
    p_Href VARCHAR,
    p_PortSpeed VARCHAR,
    p_Capacity INTEGER,
    p_PositionOnPort INTEGER,
    p_ManagementIP VARCHAR,
    p_DeviceName VARCHAR,
    p_OrderId BIGINT, -- Changed to BIGINT
    p_PlugableId BIGINT, -- Changed to BIGINT
    p_PortId BIGINT,
    inout success int,
    keys VARCHAR[], 
    p_values VARCHAR[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    d_logicalportid BIGINT;
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;
BEGIN
    -- Begin transaction
    BEGIN
        -- Insert data into logical_port table
        INSERT INTO logical_port ("name",position_on_card,position_on_device,port_type,operational_state,administrative_state,usage_state,href,port_speed,
        capacity,position_on_port,management_ip,devicename,order_id,plugableid,portid) 
        VALUES (p_logicalportName,p_positionOnCard,p_positionOnDevice,p_portType,p_OperationalState,p_AdministrativeState,p_UsageState,p_Href,p_PortSpeed,
        p_Capacity,p_PositionOnPort,p_ManagementIP,p_DeviceName,p_OrderId,p_PlugableId,p_PortId) RETURNING logicalportid INTO d_logicalportid;
        
        -- Check if both keys and values arrays have lengths greater than 0
        IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
            -- Iterate over each key-value pair and insert into the additional_attribute table
            FOR i IN 1..array_length(keys, 1) LOOP
                -- Insert into additional_attribute table and retrieve the ID
                INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                -- Append the inserted ID to the inserted_ids array
                inserted_ids := inserted_ids || ARRAY[inserted_id];
                
                -- Insert into logical_port_additional_attribute table
                INSERT INTO logical_port_additional_attribute (logical_port_id, additional_attribute_id) VALUES (d_logicalportid, inserted_id);
            END LOOP;
        END IF;
        
        -- Commit the transaction
        SUCCESS := 1;
       
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction on error
            SUCCESS := 0;
            ROLLBACK;
            RAISE;
    END;
END $$;

------------------end------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE CreatePhysicalConnection(
    p_name VARCHAR,
    p_devicea VARCHAR,
    p_devicez VARCHAR,
    p_deviceaport VARCHAR,
    p_devicezport VARCHAR,
    p_connectionType VARCHAR,
    p_bandwidth INTEGER,
    p_portnamea VARCHAR,
    p_portnameb VARCHAR,
    keys VARCHAR[], 
    p_values VARCHAR[],
    inout success int
)
LANGUAGE plpgsql
AS $$
DECLARE
    d_physicalconnection_id BIGINT;
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;
BEGIN
    -- Begin transaction
    BEGIN
        -- Insert data into physical_connection table
        INSERT INTO physical_connection (name, devicea, deviceb, deviceaport, devicezport, connection_type, bandwidth, portnamea, portnameb) 
        VALUES (p_name, p_devicea, p_devicez, p_deviceaport, p_devicezport, p_connectionType, p_bandwidth, p_portnamea, p_portnameb) 
        RETURNING physicalconnection_id INTO d_physicalconnection_id;
        
        -- Check if both keys and values arrays have lengths greater than 0
        IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
            -- Iterate over each key-value pair and insert into the additional_attribute table
            FOR i IN 1..array_length(keys, 1) LOOP
                -- Insert into additional_attribute table and retrieve the ID
                INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                -- Append the inserted ID to the inserted_ids array
                inserted_ids := inserted_ids || ARRAY[inserted_id];
                
                -- Insert into physicalconnection_additional_attribute table
                INSERT INTO physicalconnection_additional_attribute (physicalconnection_id, additional_attribute_id) 
                VALUES (d_physicalconnection_id, inserted_id);
            END LOOP;
        END IF;
        
        -- Commit the transaction
        SUCCESS := 1;
       
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction on error
            SUCCESS := 0;
            ROLLBACK;
            RAISE;
    END;
END $$;

------------------end------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE CreateLogicalConnection(
    p_name VARCHAR,
    p_devicea VARCHAR,
    p_devicez VARCHAR,
    p_deviceaport VARCHAR,
    p_devicezport VARCHAR,
    p_connectionType VARCHAR,
    p_bandwidth INTEGER,
	p_devicesConnected VARCHAR[],
	p_physicalconnection VARCHAR[],
	p_logicalportnamea VARCHAR,
    p_logicalportnameb VARCHAR,
    keys VARCHAR[], 
    p_values VARCHAR[],
    inout success int
)
LANGUAGE plpgsql
AS $$
DECLARE
    d_logicalconnection_id BIGINT;
    inserted_ids INT[] := '{}';
    inserted_id INT;
    i INT;
BEGIN
    -- Begin transaction
    BEGIN
        -- Insert data into physical_connection table
        INSERT INTO logical_connection (name, devicea, devicez, devicealogical_port, devicezlogical_port, connection_type, bandwidth, devicealogical_port_name, devicezlogical_port_name,
		devices_connected,physical_connections)
        VALUES (p_name, p_devicea, p_devicez, p_deviceaport, p_devicezport, p_connectionType, p_bandwidth, p_logicalportnamea, p_logicalportnameb,p_devicesConnected,p_physicalconnection) 
        RETURNING logicalconnection_id INTO d_logicalconnection_id;
        
        -- Check if both keys and values arrays have lengths greater than 0
        IF array_length(keys, 1) > 0 AND array_length(p_values, 1) > 0 THEN
            -- Iterate over each key-value pair and insert into the additional_attribute table
            FOR i IN 1..array_length(keys, 1) LOOP
                -- Insert into additional_attribute table and retrieve the ID
                INSERT INTO additional_attribute ("key", "value") VALUES (keys[i], p_values[i]) RETURNING id INTO inserted_id;
                -- Append the inserted ID to the inserted_ids array
                inserted_ids := inserted_ids || ARRAY[inserted_id];
                
                -- Insert into physicalconnection_additional_attribute table
                INSERT INTO logicalconnection_additional_attribute (logicalconnection_id, additional_attribute_id) 
                VALUES (d_logicalconnection_id, inserted_id);
            END LOOP;
        END IF;
        
        -- Commit the transaction
        SUCCESS := 1;
       
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction on error
            SUCCESS := 0;
            ROLLBACK;
            RAISE;
    END;
END $$;

--------------end------------------------------------------------------------------------------------
SELECT * FROM logical_connection
WHERE EXISTS (
    SELECT 1
    FROM unnest(physical_connections) AS pc
    WHERE pc::text LIKE 't%'
);
(Array serach code)
---------------end------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS change_log (
    log_id SERIAL PRIMARY KEY,
    table_name TEXT NOT NULL,
    operation TEXT NOT NULL,
    change_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    old_data JSONB,
    new_data JSONB
);


CREATE OR REPLACE FUNCTION log_table_changes() RETURNS TRIGGER AS $$
BEGIN
    -- Skip logging for the change_log table
    IF TG_TABLE_NAME = 'change_log' THEN
        RETURN NULL;
    END IF;

    IF (TG_OP = 'DELETE') THEN
        INSERT INTO change_log(table_name, operation, old_data)
        VALUES (TG_TABLE_NAME, 'DELETE', row_to_json(OLD)::jsonb);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO change_log(table_name, operation, old_data, new_data)
        VALUES (TG_TABLE_NAME, 'UPDATE', row_to_json(OLD)::jsonb, row_to_json(NEW)::jsonb);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO change_log(table_name, operation, new_data)
        VALUES (TG_TABLE_NAME, 'INSERT', row_to_json(NEW)::jsonb);
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$ LANGUAGE plpgsql;


DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public' AND tablename != 'change_log') LOOP
        EXECUTE 'CREATE TRIGGER ' || r.tablename || '_log_trigger
                 AFTER INSERT OR UPDATE OR DELETE ON ' || r.tablename ||
                ' FOR EACH ROW EXECUTE FUNCTION log_table_changes();';
    END LOOP;
END $$;



DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT event_object_table, trigger_name
              FROM information_schema.triggers
              WHERE trigger_schema = 'public' AND action_statement LIKE '%log_table_changes()%') LOOP
        EXECUTE 'DROP TRIGGER ' || r.trigger_name || ' ON ' || r.event_object_table || ';';
    END LOOP;
END $$;



-----------------------------------------------end------------------------------------------------------------------------------------



















