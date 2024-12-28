if redis.call('EXISTS', KEYS[1]) == 1 then
    return 'Exists'
else
    return 'Not Exists'
end