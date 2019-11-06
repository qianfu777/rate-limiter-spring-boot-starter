return tryAcquire(KEYS[1], KEYS[2])

function tryAcquire(key, capacity)
    local water = refreshWater(key)
    if water < capacity then
        redis.call('incr', key)
        return 1
    else
        return 0
    end
end

function refreshWater(key)
    local exist = redis.call('exists', key)
    if exist == 1 then
        local water = redis.call('get', key)
        local timeDiff = getTimeDiff(key)
        redis.call('set', key, max(0, water - timeDiff))
    else
        redis.call('set', 0)
        return 0
    end
end

function getTimeDiff(key)
    local now = redis.call('TIME')[1]
    local timeKey = key + ':TIMESTAMP'
    local exist = redis.call('exists', timeKey)
    if exist == 1 then
        local refreshTime = redis.call('get', timeKey)
        redis.call('set', timeKey, now)
        return now - refreshTime
    else
        redis.call('set', timeKey, now)
        return 0
    end
end

function max(num1, num2)
    if (num1 > num2) then
        result = num1;
    else
        result = num2;
    end
    return result;
end