using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface IAreaService
    {
        IQueryable<Area> GetAreaByCarParkId(int carParkId);
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
                    CarPark = q.CarPark,
                    CarParkId = q.CarParkId,
                    EmptyAmount = q.EmptyAmount,
                    Id = q.Id,
                    Name = q.Name,
                    ParentId = q.ParentId,
                    ParkingLots = q.ParkingLots,
                    Status = q.Status,
                    UpdateAvailable = (q.EmptyAmount == q.ParkingLots.Count ? true : false),
                });
        }
    }

    public class AreaCustom : Area
    {
        public bool UpdateAvailable { get; set; }
    }
}