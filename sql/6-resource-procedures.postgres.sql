CREATE OR REPLACE PROCEDURE "AddOrUpdateResource"(
  Id varchar(100),
  Name varchar(250)
)
AS $$
BEGIN
    UPDATE "Resource"
    SET "Name" = COALESCE(Name, Id)
    WHERE "Id" = Id;

    IF FOUND THEN
        RETURN;
    END IF;

    INSERT INTO "Resource" ("Id", "Name")
    VALUES (Id, COALESCE(Name, Id));

    RETURN;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE "RemoveResource"(
    Id VARCHAR(100)
)
AS $$
BEGIN
    DELETE FROM "Resource" WHERE "Id" = "RemoveResource".Id;

    IF FOUND THEN
        PERFORM "RemoveRelatedEdges"(Id, 'Resource');
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION "GetResourcesList"()
RETURNS TABLE (
    "Id" VARCHAR,
    "Name" VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT resource."Id",
           resource."Name"
    FROM "Resource" resource;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION "GetSubResourcesList"(
    ResourceId VARCHAR(100),
    IsFlat INT
)
RETURNS TABLE (
    "Id" VARCHAR,
    "Name" VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
           R."Id",
           R."Name"
    FROM "Resource" R
    INNER JOIN "Edge" E ON R."Id" = E."StartVertex"
    WHERE E."EndVertex" = ResourceId
        AND E."Source" = 'Resource'
        AND (IsFlat = 1 OR E."Hops" = IsFlat);
END;
$$ LANGUAGE plpgsql;