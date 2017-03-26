using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface IAreaService
    {
        IQueryable<Area> GetAreaByCarParkId(int carParkId);
        AreaCustom GetAreaWithEmptyNumber(int areaId);
    }

    public partial class AreaService
    {
        public IQueryable<Area> GetAreaByCarParkId(int carParkId)
        {
            return this.GetActive(q => q.CarParkId == carParkId)
                .Select(q => new AreaCustom
                {
                    Active = q.Active,
                    Area1 = q.Area1,
                    Area2 = q.Area2,
                    Address = q.Address,
                    CarPark = q.CarPark,
                    CarParkId = q.CarParkId,
                    EmptyAmount = q.ParkingLots.Count(a => a.Status == (int)ParkingLotStatus.Active),
                    Id = q.Id,
                    Name = q.Name,
                    ParentId = q.ParentId,
                    ParkingLots = q.ParkingLots,
                    Status = q.Status,
                    UpdateAvailable = (q.ParkingLots.Count(a => a.Status == (int)ParkingLotStatus.Reserved 
                        || a.Status == (int)ParkingLotStatus.Nonavailable) > 0),
                });
        }

        public AreaCustom GetAreaWithEmptyNumber(int areaId)
        {
            var entity = this.Get(areaId);
            var result = new AreaCustom
            {
                Active = entity.Active,
                Area1 = entity.Area1,
                Area2 = entity.Area2,
                Address = entity.Address,
                CarPark = entity.CarPark,
                CarParkId = entity.CarParkId,
                Id = entity.Id,
                Name = entity.Name,
                ParentId = entity.ParentId,
                ParkingLots = entity.ParkingLots,
                Status = entity.Status,
                EmptyAmount = entity.ParkingLots.Count(q => q.Status == (int)ParkingLotStatus.Active),
            };
            return result;
        }
    }

    public class AreaCustom : Area
    {
        public int EmptyAmount { get; set; }
        public bool UpdateAvailable { get; set; }
    }
}